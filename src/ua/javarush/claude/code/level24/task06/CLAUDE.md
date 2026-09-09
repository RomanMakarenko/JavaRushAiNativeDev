# Commerce OS — інструкції для Claude Code

## Про проєкт

Сервіс обробляє замовлення, платежі та refund flow. Стек: Java + Spring Boot,
PostgreSQL, міграції в `db/migration`. Основна гілка — `main` (protected).

## Команда та правила

- Перед будь-яким PR проганяй локальні quality gates (див. `docs/QUALITY_GATES.md`).
- Не роби push напряму в protected branch `main` — лише через PR з review.
- Не чіпай файли із секретами (`.env`, `secrets/`) без явного узгодження.
- Зміни схеми БД (`db/migration/**`) завжди потребують review.

## Чого тут поки що немає

Єдиного документа з AI coding policy немає — правила рознесені по `CLAUDE.md`,
`.claude/settings.json`, PR template і усних домовленостях.