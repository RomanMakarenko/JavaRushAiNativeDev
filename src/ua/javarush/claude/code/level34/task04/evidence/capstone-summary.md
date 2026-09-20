# Capstone Summary — короткий опис випускного проекту

Платіжний backend-сервіс на Java/Spring Boot, виконаний як capstone курсу.

## Що всередині

- REST API: каталог товарів, оформлення замовлення, refund flow.
- Сховище: PostgreSQL + Flyway-міграції.
- Тести: JUnit (unit-тести бізнес-правил, один integration-тест на refund flow).
- Інфраструктура: Docker і docker-compose для локального запуску.
- CI: GitHub Actions з PR-check (build + test).

## Що підтверджує

- Реальний issue-to-PR цикл з code review.
- Уміння довести фічу від розбору задачі до merge.
- Базова робота з контейнеризацією та CI.

## Чого немає

- Черг повідомлень (Kafka/RabbitMQ) у проекті не використовувалося.
- Метрик/моніторингу, крім логування, не налаштовувалося.
