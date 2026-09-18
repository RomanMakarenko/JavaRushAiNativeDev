# demo-ready-api

Невеликий FastAPI-сервіс для демонстрації capstone-проєкту. Приймає запит на аналіз
транзакцій і повертає коротке зведення. Запускається локально через Docker Compose.

## Що всередині

- `app/main.py` — FastAPI-застосунок із трьома ендпоінтами:
  - `GET /api/health` — перевірка живості сервісу.
  - `GET /api/demo-summary` — готове demo-зведення за прикладними даними.
  - `POST /api/analyze` — аналіз переданого запиту (основний потік).
- `data/sample-request.json` — приклад вхідного запиту для `/api/analyze`.
- `docker-compose.yml` — піднімає сервіс на локальному порту `8080`.

## Локальний запуск

```
docker compose up -d
curl http://localhost:8080/api/health
```

Сервіс слухає порт `8080` на хості (всередині контейнера — `8000`).

## Сценарій із прикладними даними

Основний потік: надіслати `data/sample-request.json` у `POST /api/analyze`
і отримати зведення з полями `total`, `top_category`, `status`.