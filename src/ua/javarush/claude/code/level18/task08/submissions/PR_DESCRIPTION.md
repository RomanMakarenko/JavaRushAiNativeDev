# PR Description — issue-432: порожній кошик повертає 400 замість 500

## What changed
- `src/main/java/com/example/commerce/orders/OrderController.java`: у `createOrder` додано ранню перевірку кошика — якщо `items` дорівнює `null` або порожній, ендпоінт одразу повертає `400 Bad Request` із тілом `Cart is empty`, не доходячи до обчислення суми. Поведінка для непорожнього кошика не змінюється.
- `src/test/java/com/example/commerce/orders/OrderControllerTest.java`: додано тест `returnsBadRequestForEmptyCart` — `POST /api/orders` із `items: []` має повертати статус `400`.

## Why
Issue-432: запит `POST /api/orders` із порожнім списком позицій (`items: []`) раніше завершувався `500 Internal Server Error` — падіння ставалося під час обчислення суми за порожнім списком. Порожній кошик — це помилка клієнта, тож за поточною очікуваною поведінкою ендпоінт має відповідати `400 Bad Request` із тілом `Cart is empty`, і перевірка має спрацьовувати до будь-якого обчислення суми.

## How to test
1. Запустити цільовий тест:
   `./gradlew :orders:test --tests com.example.commerce.orders.OrderControllerTest.returnsBadRequestForEmptyCart`
   Очікується успішне проходження тесту `returnsBadRequestForEmptyCart` (статус `400`).
2. Ручна перевірка ендпоінта згідно з diff summary:
   - `POST /api/orders` із тілом `{"items": []}` → очікується `400 Bad Request`, тіло відповіді `Cart is empty`.

## Risks / edge cases
- Перевірка покриває і `items: null` (наприклад, тіло `{}`), і порожній список — обидва варіанти трактуються як порожній кошик і повертають `400`.
- Для непорожнього кошика поведінка не змінюється: обчислення суми та контракт успішної відповіді залишаються без змін.
- `400` повертається з точним тілом `Cart is empty`; інших змін контракту відповіді в цьому PR немає.

## Out of scope
- Валідація невалідної кількості позицій (`quantity <= 0`) — обробляється окремим issue.
- Зміни схеми API та контрактів відповіді для непорожнього кошика.
- Рефакторинг обчислення суми замовлення — у код зачіпається лише рання перевірка кошика.