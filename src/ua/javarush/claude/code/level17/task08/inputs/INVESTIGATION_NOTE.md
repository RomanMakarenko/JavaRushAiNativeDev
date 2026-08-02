# Investigation Note

## Summary
Сповіщення збираються в `NotificationService.listForUser()`, який викликає `NotificationRepository.findByUser()`. Явне сортування відсутнє, тому порядок визначається тим, як БД повернула рядки. Контролер `NotificationController.list()` повертає результат сервісу без змін.

## Entry point
- `NotificationController.list()` — обробляє `GET /api/notifications`.

## Relevant files
- `src/main/java/com/rush/notifications/NotificationService.java`
- `src/main/java/com/rush/notifications/NotificationRepository.java`

## Related tests
- `src/test/java/com/rush/notifications/NotificationServiceTest.java`

## Unknowns
- Не перевірено, чи вже є сортування на рівні SQL-запиту в мапінгу репозиторію.
- Невідомо, чи потрібен окремий пріоритет для unread поверх дати створення.

## Risk notes
- medium: зміна порядку може зачепити кеш на клієнті, розрахований на поточну видачу.