# Ticket Status Service — mini-capstone

Невеликий сервіс на FastAPI, який повертає статус заявок підтримки та health-check.
Це підсумковий capstone-проєкт курсу, який потрібно підготувати до здачі (submission package).

## Що всередині

- HTTP API зі статусами заявок та ендпоінтом `/health`.
- Запуск через Docker Compose.
- Тести на health-check.

## Запуск

```bash
cp .env.example .env
docker compose up -d --build
curl http://localhost:18080/health
```

Докладний сценарій демонстрації описано в `docs/DEMO.md`.
Повний опис поведінки — в `SPEC.md`, підтвердження результату — в `EVIDENCE.md`.