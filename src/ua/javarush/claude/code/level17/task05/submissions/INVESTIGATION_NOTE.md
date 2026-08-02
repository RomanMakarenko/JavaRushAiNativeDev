# Investigation Note — Issue #517

> Повернення купона: `500 Internal Server Error` замість контрольованої помилки валідації.
> Read-only дослідження, зміни коду не вносились.

## Summary

`POST /api/coupons/return` із `{"couponCode": null}` (або без поля `couponCode`)
призводить до `NullPointerException` у `ReturnService.processReturn` — у коді немає
null-guard перед `couponCode.trim()`. Водночас `ReturnExceptionHandler` обробляє лише
`IllegalArgumentException`, тому `NullPointerException` (і навіть `IllegalStateException`
з порожнім рядком) не перехоплюється і доходить до клієнта як `500`.

У дефекті дві незалежні причини: **відсутність null-перевірки в сервісі** та
**неповне покриття винятків в обробнику помилок**. Тести цю гілку не покривають.

## Relevant files

- `src/main/java/com/example/store/returns/ReturnController.java` — REST-контролер, маршрут `POST /api/coupons/return`.
- `src/main/java/com/example/store/returns/ReturnService.java` — бізнес-логіка повернення, місце NPE.
- `src/main/java/com/example/store/returns/ReturnExceptionHandler.java` — `@RestControllerAdvice`, трансляція винятків у HTTP-статуси.
- `src/main/java/com/example/store/returns/ReturnRequest.java` — вхідний record (`couponCode`).
- `src/main/java/com/example/store/returns/ReturnResponse.java` — вихідний record (не винен у дефекті).
- `src/test/java/com/example/store/returns/ReturnControllerTest.java` — тести модуля (покриття неповне).

## Entry points

| # | Точка входу | Місце в коді |
|---|---|---|
| 1 | `POST /api/coupons/return` → `ReturnController.returnCoupon(ReturnRequest)` | `ReturnController.java:16-20` |
| 2 | `ReturnService.processReturn(String couponCode)` — сюди одразу передається `request.couponCode()` | `ReturnController.java:19`, `ReturnService.java:9-16` |
| 3 | `ReturnExceptionHandler.handleValidation(IllegalArgumentException)` — єдиний активний обробник винятків | `ReturnExceptionHandler.java:13-16` |

## Related tests

- `ReturnControllerTest.processReturnMarksCouponRefunded` — тільки happy path
  (`ReturnControllerTest.java:12-16`): викликає `processReturn("SAVE10")` і перевіряє `refunded() == true`.
- **Прогалина в покритті:** немає жодного тесту на `null` або порожній `couponCode`.
  Пряма вказівка на це — коментар у `ReturnControllerTest.java:18`.
- У межах зрізу немає build-файла (`pom.xml`/`build.gradle`), тому тести локально не запускались
  і немає `@SpringBootTest`/`MockMvc`-тесту на рівні HTTP — поведінка обробника помилок не перевіряється тестами взагалі.

## Likely change points

(Тільки гіпотези для наступного кроку — код не змінювався.)

1. **`ReturnService.java:12`** — додати null-guard перед `couponCode.trim()`:
   `if (couponCode == null || couponCode.trim().isEmpty())`. Це усуває NPE.
2. **`ReturnService.java:13`** — порожній код кидає `IllegalStateException("Купон уже повернуто")`,
   що (а) не обробляється → 500, і (б) семантично некоректно: «купон уже повернуто» — це не те саме,
   що «код порожній». Найімовірніше, тут має бути `IllegalArgumentException` (→ 400) або
   окремий виняток для «купон не знайдено».
3. **`ReturnExceptionHandler.java`** — розширити обробку:
   - `@ExceptionHandler(NullPointerException.class)` / `IllegalStateException.class` → `400` із поясненням;
   - або загальний fallback-обробник `Exception` → контрольований `4xx/5xx` без витоку стектрейса.
4. **`ReturnController.java:17` + `ReturnRequest.java:4`** — ідіоматичний Spring-шлях: додати
   `@Valid` на параметр і `@NotBlank` на `couponCode` (jakarta.validation), якщо в проєкті є
   validation-стартер. Це перекладе валідацію на `MethodArgumentNotValidException` → 400.

## Unknowns

- Який саме HTTP-код і текст помилки очікує frontend для цього кейсу (питання 1 з issue)?
- Чи потрібно окремо розрізняти «купон не знайдено» і «купон уже повернуто» (питання 2 з issue)?
  Зараз обидва випадки відсутні як такі — є лише `IllegalStateException` для порожнього коду.
- Чи в межах фіксу кейс `{}` (поле `couponCode` відсутнє в тілі)? Він іде тим самим шляхом NPE.
- Чи є десь upstream-валідація (gateway/фільтр), яка зазвичай перехоплює порожній `couponCode` —
  тобто чому дефект «іноді», а не завжди? У межах зрізу такого шару немає.
- Версія Spring/Java та наявність validation-залежностей — у зрізі немає build-файла.

## Evidence

Ланцюжок дефекту з посиланнями на код:

1. `ReturnRequest.java:4` — `record ReturnRequest(String couponCode)`. Jackson при
   `{"couponCode": null}` (або при відсутньому полі) кладе в `couponCode` значення `null`.
2. `ReturnController.java:19` — `returnService.processReturn(request.couponCode())`:
   `null` передається в сервіс без будь-якої перевірки.
3. `ReturnService.java:12` — `if (couponCode.trim().isEmpty())`: виклик `.trim()` на `null`
   кидає `NullPointerException`. **Саме це і бачимо в логах (див. issue).**
4. `ReturnService.java:13` — якщо код — не `null`, а порожній рядок, кидається
   `IllegalStateException("Купон уже повернуто")`. Цей виняток теж не обробляється.
5. `ReturnExceptionHandler.java:12-13` — обробляється **тільки** `IllegalArgumentException`
   (коментар прямо фіксує це). `NullPointerException` та `IllegalStateException`
   випадають у default-обробку Spring → `500 Internal Server Error`. Збігається з симптомом.
6. `ReturnControllerTest.java:12-18` — покриття лише happy path; гілка валідації не тестується,
   тому дефект міг «проскочити» регресію.

**Висновок:** симптом відтворюється суто в межах цього модуля. Мінімальний фікс —
null-guard у `ReturnService.processReturn` (п. 3) + обробка відповідних винятків у
`ReturnExceptionHandler` (п. 5). Рішення щодо розділення «не знайдено» / «уже повернуто»
і контракту з frontend лишається за бізнес-вимогами (див. Unknowns).