# DEMO — сценарій демонстрації

1. Скопіювати приклад середовища: `cp .env.example .env`.
2. Підняти стек: `docker compose up -d --build`.
3. Перевірити health-check: `curl http://localhost:18080/health` — очікується `{"status":"ok"}`.
4. Запросити статус заявки: `curl http://localhost:18080/tickets/T-100`.
5. Зупинити стек: `docker compose down`.