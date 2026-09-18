# Сервіс звернень до служби підтримки (capstone)

Невеликий сервіс прийому звернень до підтримки. Це пакет подання для фінального захисту.

## Запуск

```bash
cp .env.example .env
docker compose up -d --build
curl http://localhost:8085/health
```

## Створення тікета

```bash
curl -X POST http://localhost:8085/tickets -d '{"message": "не надходить лист"}'
```

## Тести

```bash
pytest -q
```