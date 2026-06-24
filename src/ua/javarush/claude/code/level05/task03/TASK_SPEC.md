# TASK_SPEC: AUTH-318 — сесія скидається після refresh токена

## Goal
Після оновлення access-токена користувач має залишатися авторизованим.
Наразі приблизно у 5% випадків одразу після refresh сесія вважається недійсною
і користувача викидає на екран входу.

## Affected area
- `src/auth/LoginController.java`
- `src/auth/SessionService.java`

## How to reproduce
1. Увійти в Commerce OS.
2. Дочекатися завершення терміну дії access-токена (або примусово викликати refresh).
3. Зробити будь-який авторизований запит одразу після refresh.

## Out of scope
- Модуль замовлень (`src/orders/`).
- UI екрана входу.