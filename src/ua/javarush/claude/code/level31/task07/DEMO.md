# DEMO

## Налаштування

- Потрібні Docker і Docker Compose з підтримкою команди `docker compose`.
- Усі команди виконуються з каталогу `src/ua/javarush/claude/code/level31/task07`.
- Compose-файл запускає сервіс `api` з образом `demo-ready-api:local`.
- Локальний порт хоста `8080` прокинуто на порт `8000` контейнера.
- Середовище контейнера: `APP_ENV=local`, рівень журналювання: `LOG_LEVEL=info`.
- Для демонстрації використовується лише приклад із `data/sample-request.json`; production data не потрібні.

## Команда для запуску

```bash
docker compose up -d --build
curl http://localhost:8080/api/health
```

Очікувана відповідь перевірки живості:

```json
{"status":"ok","env":"local"}
```

## Demo-дані

Файл `data/sample-request.json` містить локальний запит для користувача `demo-user-001` із п’ятьма транзакціями:

- `groceries`: 54.20, 71.35 і 40.10;
- `transport`: 18.00;
- `entertainment`: 32.50.

Надіслати ці дані можна без копіювання JSON у командний рядок:

```bash
curl -X POST http://localhost:8080/api/analyze \
  -H 'Content-Type: application/json' \
  --data @data/sample-request.json
```

## Кроки сценарію

1. Перейти до каталогу `src/ua/javarush/claude/code/level31/task07`.
2. Запустити локальний контейнер командою `docker compose up -d --build`.
3. Перевірити доступність сервісу через `curl http://localhost:8080/api/health`.
4. Надіслати `data/sample-request.json` до `POST /api/analyze` командою з розділу «Demo-дані».
5. Показати у відповіді ідентифікатор `demo-user-001` і сформоване зведення.
6. Після демонстрації зупинити контейнер командою `docker compose down`.

## Очікуваний результат

Основний запит повертає JSON такого змісту:

```json
{
  "user_id": "demo-user-001",
  "summary": {
    "total": 216.15,
    "top_category": "groceries",
    "status": "ok"
  }
}
```

Сума `216.15` — це загальна сума п’яти локальних demo-транзакцій, а `groceries` — категорія з найбільшою сумою (`165.65`).

## Резервний варіант

Якщо основний потік не запускається, використати такий трирівневий ланцюжок:

1. **Перезапуск контейнера:** виконати `docker compose down`, потім `docker compose up -d --build` і повторити `curl http://localhost:8080/api/health`.
2. **Перевірка готового зведення:** якщо сервіс живий, але надсилання файлу незручне, виконати `curl http://localhost:8080/api/demo-summary` — цей ендпоінт читає той самий локальний `data/sample-request.json`.
3. **Перевірка без Docker:** якщо Docker недоступний, створити локальне Python-оточення, встановити залежності з `requirements.txt` командою `python -m pip install -r requirements.txt`, запустити `uvicorn app.main:app --host 127.0.0.1 --port 8080`, а потім повторити локальний `curl` із основного сценарію.

У кожному варіанті використовуються тільки файли репозиторію та тестові demo-дані; production data не залучаються.

## Що пояснити

- `/api/health` підтверджує, що контейнер доступний і працює в `local`-середовищі.
- `/api/analyze` є основним потоком: приймає `user_id` і список транзакцій та повертає коротке зведення.
- `total` обчислюється як сума полів `amount`, а `top_category` — як категорія з найбільшою сумою.
- `/api/demo-summary` — резервна демонстраційна точка, яка бере ті самі дані з файлу всередині контейнера.
- Порт `8080` — зовнішній локальний порт, тоді як FastAPI всередині контейнера слухає `8000`.