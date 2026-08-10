# Step 1 Request — orders: limit за замовчуванням

## Current step

Крок 1 із 3 (затверджений план: `docs/APPROVED_PLAN.md`).

`GET /api/orders` має використовувати `limit=50`, якщо query-параметр `limit` не передано. Тобто `OrderController.list(...)` при `limit == null` має передавати `orderService.findRecent(50)`.

## Allowed files

- `src/main/java/com/example/commerce/orders/OrderController.java`

Жодні інші файли (включно з тестами, `OrderService`, `Order`, build-конфігурацією) не змінюються в межах цього кроку.

## Targeted check

```
./gradlew :orders:test --tests OrderControllerTest.usesDefaultLimitWhenMissing
```

## Stop rules

- Виконується рівно один крок за ітерацію — крок 1, без забігання наперед до кроків 2 і 3.
- Змінюються лише файли зі списку Allowed files цього кроку.
- Після edits запускається лише targeted check цього кроку, а не весь suite.
- Крок вважається завершеним, лише коли targeted check проходить успішно.