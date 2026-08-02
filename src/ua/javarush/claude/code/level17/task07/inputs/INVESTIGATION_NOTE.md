# Investigation Note

## Summary
Список замовлень формується в `OrderService.listOrders()`, який викликає `OrderRepository.findAllForUser()`. Сортування ніде явно не задано: репозиторій повертає замовлення в порядку вставки, а сервіс віддає їх як є. Контролер `OrderController.getOrders()` просто передає результат сервісу.

## Entry point
- `OrderController.getOrders()` — обробляє `GET /api/orders`.

## Relevant files
- `src/main/java/com/rush/orders/OrderController.java`
- `src/main/java/com/rush/orders/OrderService.java`
- `src/main/java/com/rush/orders/OrderRepository.java`

## Related tests
- `src/test/java/com/rush/orders/OrderServiceTest.java`
- `src/test/java/com/rush/orders/OrderControllerTest.java`

## Unknowns
- Не підтверджено, чи є індекс за `created_at` у БД (це може вплинути на продуктивність сортування).
- Невідомо, чи використовується десь іще `findAllForUser()` з розрахунком на поточний порядок.
- Не перевірено поведінку для порожнього списку замовлень.