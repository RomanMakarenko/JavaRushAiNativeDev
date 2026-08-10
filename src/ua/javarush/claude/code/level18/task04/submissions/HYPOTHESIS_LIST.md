# HYPOTHESIS_LIST — checkout порожнього кошика повертає 500

Джерела доказів: `materials/issue.md`, `materials/error.log`, `materials/recent-diff.patch`.
Документ описує лише гіпотези та докази; готового коду виправлення тут немає.

## Symptom

`POST /api/orders/checkout` із тілом `{"items":[]}` повертає HTTP 500 замість очікуваного
HTTP 400 із повідомленням "cart is empty".

- issue.md: «сервіс повертає HTTP 500 Internal Server Error. Очікувався HTTP 400 із зрозумілим
  повідомленням "cart is empty"».
- error.log: `ERROR c.e.commerce.orders.OrderController : checkout failed with 500`.
- error.log: `java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0` у
  `CheckoutService.price(CheckoutService.java:24)`.
- Відтворюється стабільно для будь-якого запиту з `items=0` (issue.md).

## Ranked hypotheses

1. **H1 — Регресія в `price()`: коміт «simplify cart pricing» прибрав захист від порожнього кошика.**
   У `CheckoutService.price()` було видалено перевірку `items == null || items.isEmpty()` разом із
   `throw new EmptyCartException("cart is empty")`. Тепер для порожнього списку виконується
   `items.get(0)`, що кидає `IndexOutOfBoundsException`. Саме ця виняткова ситуація з'являється
   у логах і завершується 500.

2. **H2 — Відсутня трансляція порожнього кошика в HTTP 400 на рівні API.**
   Жоден шар (контролер або глобальний обробник винятків) не перетворює ситуацію «кошик порожній»
   на відповідь 400. Будь-яке необроблене виняткової ситуації від `price()` спливає нагору як 500;
   контракт «400 + "cart is empty"» ніде не реалізований.

## Evidence

**Для H1:**
- recent-diff.patch (зміни в `CheckoutService.java`):
  `-        if (items == null || items.isEmpty()) {` та `-            throw new EmptyCartException("cart is empty");` — перевірку видалено.
- recent-diff.patch: `+        CartItem first = items.get(0);` — перший елемент читається без жодної перевірки розміру списку.
- error.log: `java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0`
  `at com.example.commerce.orders.CheckoutService.price(CheckoutService.java:24)` — падіння саме на читанні елемента 0 порожнього списку.
- error.log: `ERROR c.e.commerce.orders.OrderController : checkout failed with 500` — необроблений виняток перетворився на 500.

**Для H2:**
- issue.md: «Очікувався HTTP 400 із зрозумілим повідомленням "cart is empty"» — очікування контракту 400, але в матеріалах немає жодних доказів, що такий мапінг існує.
- error.log: `ERROR c.e.commerce.orders.OrderController : checkout failed with 500` — фінальна відповідь 500, а не 400, тобто жоден механізм не перехопив ситуацію порожнього кошика.
- recent-diff.patch: до коміту порожній кошик оброблявся саме всередині `price()` через `EmptyCartException` — інших місць обробки цієї ситуації у наданих матеріалах немає.

## Smallest confirming check

1. Під профілем `test` надіслати `POST /api/orders/checkout` із `{"items":[]}` і звірити stack trace
   з `error.log`: якщо виняток — саме `IndexOutOfBoundsException` з `CheckoutService.price(...):24`
   (рядок `items.get(0)`), то підтверджується H1 (регресія в `price()`).
2. Перевірити, чи існує у проєкті мапінг `EmptyCartException` (наприклад, `@ControllerAdvice` /
   `@ExceptionHandler`) і чи компілюється/використовується `EmptyCartException` після коміту:
   якщо мапінгу на 400 немає або `EmptyCartException` більше ніде не кидається, підтверджується H2
   (відсутність трансляції в 400) — і повне виправлення потребує роботи не лише в `price()`.
3. Опційно: переглянути актуальний код `CheckoutService.java:24` у робочій копії, щоб переконатися,
   що рядок збігається з `items.get(0)` саме після коміту `9f12ab3`.

## Unknowns

- Чи існує у проєкті глобальний обробник винятків (`@ControllerAdvice` / `@ExceptionHandler`),
  який має перетворювати `EmptyCartException` або `IndexOutOfBoundsException` на 400 — матеріали
  цього не показують.
- Який точно код зараз стоїть у `CheckoutService.java` на рядку 24 (чи відповідає `items.get(0)`).
- Чи існує валідація порожнього списку на рівні контролера (наприклад, `@Valid`, ручна перевірка
  `items`) поза `price()` — у матеріалах її не видно.
- Чи все ще використовується клас `EmptyCartException` у проєкті після коміту «simplify cart pricing»,
  чи він став мертвим.
- Чи відповідає `error.log` останньому стану коду (після коміту `9f12ab3`), чи записаний раніше.
- Який тестовий набір покриває сценарій порожнього кошика (product tests не надано).
- Що саме має бути в відповіді 400 («cart is empty» чи інше повідомлення) — з матеріалів відомий лише
  очікуваний статус і повідомлення з issue.md.