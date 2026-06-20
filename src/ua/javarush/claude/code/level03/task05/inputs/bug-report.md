# Звіт про помилку: POST /api/login

## Звідки надійшло
Сервіс Commerce OS, модуль авторизації (`auth-service`). Скарга від команди підтримки
після релізу `2026.05.3`. На login зав’язаний refund flow: поки оператор не авторизований,
він не може відкрити форму повернення.

## Спостережувана поведінка
- При запиті `POST /api/login` з правильним логіном, але неправильним паролем сервер
  відповідає `500 Internal Server Error` з порожнім тілом.
- У логах видно рядок `NullPointerException` у `AuthService.authenticate`.
- За правильних логіна і пароля вхід проходить, видається session token.
- Відтворюється стабільно: 14 звернень у підтримку за дві доби.

## Кроки відтворення
1. Надіслати `POST /api/login` з тілом `{"login":"operator","password":"wrong"}`.
2. Отримати `500` і порожню відповідь.
3. Повторити з правильним паролем — вхід спрацьовує.

## Фрагмент логу
```
2026-05-29T11:04:18Z ERROR c.e.auth.AuthService - login failed
java.lang.NullPointerException: Cannot invoke "User.getPasswordHash()" because "user" is null
    at com.example.auth.AuthService.authenticate(AuthService.java:42)
    at com.example.auth.LoginController.login(LoginController.java:28)
```

## Коментар розробника
"Схоже, за відсутності збігу за паролем ми десь смикаємо null і не віддаємо
нормальний 401. Але це я ще не перевіряв по коду."