# Поточний стан — сервіс cashflow

Сервіс `cashflow` керує підписками та білінгом.

- Збірка: Gradle, Java 11.
- Framework: Spring Boot 2.7.18.
- Пакети: `javax.*` (persistence, validation, servlet).
- Сховище: PostgreSQL, міграції через Flyway 8 (V1..V12).
- Серіалізація JSON: Jackson, кастомний `JacksonConfig` фіксує часовий пояс `UTC`.

## Спостережувана поведінка API

- `GET /api/v1/subscriptions/{id}` — повернення підписки.
- `GET /api/v1/subscriptions` — список підписок.
- Дати у відповідях серіалізуються як ISO-8601 у зоні `UTC`.
- Бізнес-помилки повертаються з кодами з `BillingErrorCode`
  (наприклад `BILLING_ACCOUNT_SUSPENDED`, `SUBSCRIPTION_NOT_FOUND`).

## Мета команди

Перейти на Spring Boot 3.2, Java 17, пакети `jakarta.*`, Flyway 9.
У цьому уроці виконується лише планування, не сама міграція.