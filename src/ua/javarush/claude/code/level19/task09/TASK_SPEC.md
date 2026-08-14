# TASK_SPEC — дизайн крайових випадків для повернення коштів

## Goal
Підготувати design-матеріал для тестування endpoint повернення коштів
`POST /api/orders/{id}/refund` у сервісі Commerce OS. На цьому етапі тести не пишуться —
потрібен лише перелік крайових випадків, згрупований за категоріями ризику.

## Scope
- Аналіз лише refund flow (`RefundController`, `RefundService`).
- Endpoint: `POST /api/orders/{id}/refund`, тіло запиту містить `amount`, `reason`.
- Роль, що надає право на повернення: `REFUND_MANAGER`.

## Non-goals
- Не писати тестовий код.
- Не змінювати production-файли.
- Не покривати сусідні модулі (checkout, wishlist).

## Категорії ризику для enumeration
- invalid input
- boundary
- permissions
- duplicate request
- timeout
- timezone