# demo-ready-api

Невеликий FastAPI-сервіс для capstone-демонстрації. Приймає запит на аналіз
транзакцій і повертає короткий підсумок. Запускається локально через Docker Compose.

## Що всередині

- `app/main.py` — FastAPI-застосунок із трьома ендпоінтами:
  - `GET /api/health` — перевірка живості сервісу.
  - `GET /api/demo-summary` — готовий demo-підсумок за sample-даними.
  - `POST /api/analyze` — аналіз переданого запиту (основний core flow).
- `data/sample-request.json` — приклад вхідного запиту для `/api/analyze`.
- `docker-compose.yml` — запускає сервіс на локальному порту `8080`.

## Локальний запуск

```
docker compose up -d
curl http://localhost:8080/api/health
```

Сервіс слухає порт `8080` на хості (усередині контейнера — `8000`).

## Приклад сценарію

Основний потік: надіслати `data/sample-request.json` у `POST /api/analyze`
і отримати підсумок із полями `total`, `top_category`, `status`.