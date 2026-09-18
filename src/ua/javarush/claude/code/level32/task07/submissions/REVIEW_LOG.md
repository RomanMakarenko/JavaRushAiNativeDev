# REVIEW LOG — Ticket Triage Service

Джерело: mentor review перед фінальним захистом. Зауваження згруповані за напрямами review.
Кожне зауваження позначене severity: `blocking` (руйнує довіру до результату), `should` (помітно посилює proof-of-work), `nice` (розвиток на потім).

## Working result
- R1 [should] Core flow «створити тикет → отримати тикет» працює, але happy-path ніде не зафіксований як відтворюваний сценарій. Демонстрація була тільки усною.

## Evidence
- R2 [blocking] У репозиторії немає evidence того, що застосунок піднімається «з нуля». Reviewer запускав вручну, лог clean start не прикладено.
- R3 [should] Скриншоти відповідей API застаріли й не збігаються з поточною схемою відповіді (немає поля `status`).

## Checks / tests
- R4 [blocking] `pytest -q` падає: тест `test_empty_message` очікує `400`, а endpoint віддає `500` на порожній `message`. Регресійний тест на порожній ввід по суті червоний.
- R5 [should] Немає тесту на core flow цілком (створення + читання тикета), покриття точкове.

## Reproducibility
- R6 [blocking] `docker compose up -d --build` не піднімає сервіс: `docker-compose.yml` чекає змінну `APP_HOST_PORT`, а `.env.example` містить `HOST_PORT`. Reviewer не зміг відкрити `http://localhost:8085/health`.

## Documentation
- R7 [should] У `README.md` кроки запуску не відображають змінні оточення, нова людина не пройде інструкцію без підказки.
- R8 [nice] Немає короткого опису архітектури (де зберігаються тикети, який шар за що відповідає).

## Trust boundary
- R9 [should] У `.env.example` лежить правдоподібний токен замість плейсхолдера — незрозуміло, секрет це чи зразок.

## Improvement priorities
- R10 [nice] Хочеться CI, який сам ганяє `pytest` і smoke на кожен push, але для поточного захисту це не обов'язково.
- R11 [nice] Ідея: винести валідацію вхідних даних в окремий шар, щоб перевикористати в майбутніх endpoint.
