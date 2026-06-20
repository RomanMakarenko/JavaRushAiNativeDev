# Facts-first аналіз — POST /api/login

Джерело: `inputs/bug-report.md`  
Сервіс: Commerce OS, модуль авторизації (`auth-service`)  
Реліз: `2026.05.3`  
Дата аналізу: 2026-06-20

---

## Current behavior

- `POST /api/login` з неправильним паролем (`{"login":"operator","password":"wrong"}`) повертає `500 Internal Server Error` з порожнім тілом.
- `POST /api/login` з правильним паролем працює нормально — видається session token.
- Проблема відтворюється стабільно: 14 звернень у підтримку за дві доби.

## Desired behavior

- `POST /api/login` з неправильним паролем має повертати `401 Unauthorized` з відповідним тілом або повідомленням про помилку авторизації.
- Оператор з неправильним паролем не має отримувати session token.
- Помилка авторизації не має викликати неконтрольований виняток на сервері.

## Evidence

- **Логи**: `2026-05-29T11:04:18Z ERROR c.e.auth.AuthService - login failed`
- **Стек винятку**: `java.lang.NullPointerException: Cannot invoke "User.getPasswordHash()" because "user" is null` у `AuthService.authenticate(AuthService.java:42)`, викликаний з `LoginController.login(LoginController.java:28)`.
- **Вплив**: через помилку оператор не може відкрити форму повернення (refund flow), доки залишається неавторизованим.
- **Спосіб відтворення**: 1) `POST /api/login` з тілом `{"login":"operator","password":"wrong"}` → 2) отримати `500` з порожнім тілом.

## Hypotheses

1. **Null замість 401** — за відсутності збігу за паролем код викликає `user.getPasswordHash()` на `null` замість того, щоб повернути `401 Unauthorized`. *(Джерело: коментар розробника.)*
2. **Корінь — логіка пошуку користувача** — `NPE` виникає, бо `user` дорівнює `null`. Пошук користувача за логіном не знайшов запису, але повернув `null` замість викидання контрольованого винятку або негайної відповіді `401`.
3. **Брак захисної перевірки** — в `AuthService.authenticate` (рядок 42) відсутня перевірка `if (user == null) { return ... }` перед зверненням до `user.getPasswordHash()`.
4. **Глобальний обробник помилок не налаштований** — якби `500` проходив через `@ControllerAdvice` / `@ExceptionHandler`, відповідь мала б тіло з описом помилки, а не порожній `500`.