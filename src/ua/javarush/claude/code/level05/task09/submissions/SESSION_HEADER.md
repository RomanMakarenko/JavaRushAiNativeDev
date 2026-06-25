# SESSION HEADER

## Session
`fix-null-session-redirect`

## Goal
Виправити баг, при якому користувачі з простроченим refresh token бачать порожній екран замість перенаправлення на сторінку `/login` із повідомленням «session expired».

## Scope
- `src/auth/session.js` — функція `getActiveToken(session)`: при `session === null` функція падає з `TypeError: cannot read 'token' of null`.
- Місце виклику `getActiveToken` вище по стеку, яке має обробляти null-session і робити редирект.

## Non-goals
- **Не чіпати refund flow** і будь-яку billing-документацію.
- Не змінювати логіку перевірки прострочення токена (`isExpired`).
- Не рефакторити загальну архітектуру авторизації — лише лагодити конкретний баг редіректу.

## Open questions
- Чи є null-session очікуваним станом, чи це симптом іншої проблеми (наприклад, session не проініціалізована взагалі)?
- Чи потрібно чистити / видаляти локальний session storage при виявленні простроченого refresh token?

## Next step
Додати перевірку `if (!session)` на початку `getActiveToken`, щоб замість падіння повертати сигнал (наприклад, `null`), який вище за стеком призведе до редиректу на `/login`.