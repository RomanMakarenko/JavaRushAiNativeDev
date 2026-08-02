# Investigation Note — Issue #531

> Погашення купона: `500 Internal Server Error` замість контрольованої помилки валідації.
> Read-only дослідження, зміни коду не вносились.

## Summary

`POST /api/coupons/redeem` із `{"code": null}` призводить до `NullPointerException`
у `RedeemService.redeem` — у коді немає null-guard перед `code.length()`.
Водночас `RedeemExceptionHandler` обробляє лише `IllegalArgumentException`, тому
`NullPointerException` (і навіть `IllegalStateException` для короткого коду) не
перехоплюється і доходить до клієнта як `500`.

У дефекті дві незалежні причини: **відсутність null-перевірки в сервісі** та
**неповне покриття винятків в обробнику помилок**. Тести цю гілку не покривають.

## Relevant files

- `src/main/java/com/example/store/coupons/RedeemController.java` — REST-контролер, маршрут `POST /api/coupons/redeem`.
- `src/main/java/com/example/store/coupons/RedeemService.java` — бізнес-логіка погашення, місце NPE.
- `src/main/java/com/example/store/coupons/RedeemExceptionHandler.java` — `@RestControllerAdvice`, трансляція винятків у HTTP-статуси.
- `src/main/java/com/example/store/coupons/RedeemRequest.java` — вхідний record (`code`).
- `src/main/java/com/example/store/coupons/RedeemResponse.java` — вихідний record (не винен у дефекті).
- `src/test/java/com/example/store/coupons/RedeemControllerTest.java` — тести модуля (покриття неповне).

## Entry points

| # | Точка входу | Місце в коді |
|---|---|---|
| 1 | `POST /api/coupons/redeem` → `RedeemController.redeem(RedeemRequest)` | `RedeemController.java:16-20` |
| 2 | `RedeemService.redeem(String code)` — сюди одразу передається `request.code()` | `RedeemController.java:19`, `RedeemService.java:9-16` |
| 3 | `RedeemExceptionHandler.handleValidation(IllegalArgumentException)` — єдиний активний обробник винятків | `RedeemExceptionHandler.java:13-16` |

## Related tests

- `RedeemControllerTest.redeemMarksCouponRedeemed` — тільки happy path
  (`RedeemControllerTest.java:12-16`): викликає `redeem("SAVE10")` і перевіряє `redeemed() == true`.
- **Прогалина в покритті:** немає жодного тесту на `null` або короткий `code`.
  Пряма вказівка на це — коментар у `RedeemControllerTest.java:18`.
- У межах зрізу немає build-файла (`pom.xml`/`build.gradle`), тому тести локально не запускались
  і немає `@SpringBootTest`/`MockMvc`-тесту на рівні HTTP — поведінка обробника помилок не перевіряється тестами взагалі.

## Likely change points

(Тільки гіпотези для наступного кроку — код не змінювався.)

1. **`RedeemService.java:12`** — додати null-guard перед `code.length()`:
   `if (code == null || code.isBlank())`. Це усуває NPE; замість нього має кидатися
   `IllegalArgumentException` (→ 400).
2. **`RedeemService.java:13`** — короткий код кидає `IllegalStateException("Занадто короткий код купона")`,
   що (а) не обробляється → 500, і (б) семантично некоректно: «занадто короткий» — це
   помилка валідації, а не стан сервісу. Найімовірніше, тут має бути `IllegalArgumentException`
   (→ 400) або окремий виняток для «купон не знайдено».
3. **`RedeemExceptionHandler.java`** — розширити обробку:
   - `@ExceptionHandler(NullPointerException.class)` / `IllegalStateException.class` → `400` із поясненням;
   - або загальний fallback-обробник `Exception` → контрольований `4xx/5xx` без витоку стектрейса.
4. **`RedeemController.java:17` + `RedeemRequest.java:4`** — ідіоматичний Spring-шлях: додати
   `@Valid` на параметр і `@NotBlank` на `code` (jakarta.validation), якщо в проєкті є
   validation-стартер. Це перекладе валідацію на `MethodArgumentNotValidException` → 400.

## Unknowns

- Який саме HTTP-код і текст помилки очікує frontend для цього кейсу (питання з issue)?
- Чи потрібно окремо розрізняти «купон не знайдено» і «занадто короткий код»?
  Зараз обидва випадки відсутні як такі — є лише `IllegalStateException` для короткого коду.
- Чи в межах фіксу кейс `{}` (поле `code` відсутнє в тілі)? Він іде тим самим шляхом NPE.
- Чи є десь upstream-валідація (gateway/фільтр), яка зазвичай перехоплює порожній `code` —
  тобто чому дефект «іноді», а не завжди? У межах зрізу такого шару немає.
- Версія Spring/Java та наявність validation-залежностей — у зрізі немає build-файла.

## Evidence

Ланцюжок дефекту з посиланнями на код:

1. `RedeemRequest.java:4` — `record RedeemRequest(String code)`. Jackson при
   `{"code": null}` (або при відсутньому полі) кладе в `code` значення `null`.
2. `RedeemController.java:19` — `redeemService.redeem(request.code())`:
   `null` передається в сервіс без будь-якої перевірки.
3. `RedeemService.java:12` — `if (code.length() < 3)`: виклик `.length()` на `null`
   кидає `NullPointerException`. **Саме це і бачимо в логах (див. issue).**
4. `RedeemService.java:13` — якщо код — не `null`, а короткий рядок (< 3 символів),
   кидається `IllegalStateException("Занадто короткий код купона")`. Цей виняток теж не обробляється.
5. `RedeemExceptionHandler.java:12-13` — обробляється **тільки** `IllegalArgumentException`
   (коментар прямо фіксує це). `NullPointerException` та `IllegalStateException`
   випадають у default-обробку Spring → `500 Internal Server Error`. Збігається з симптомом.
6. `RedeemControllerTest.java:12-18` — покриття лише happy path; гілка валідації не тестується,
   тому дефект міг «проскочити» регресію.

**Висновок:** симптом відтворюється суто в межах цього модуля. Мінімальний фікс —
null-guard у `RedeemService.redeem` (п. 3) + обробка відповідних винятків у
`RedeemExceptionHandler` (п. 5). Рішення щодо розділення «не знайдено» / «короткий код»
і контракту з frontend лишається за бізнес-вимогами (див. Unknowns).
