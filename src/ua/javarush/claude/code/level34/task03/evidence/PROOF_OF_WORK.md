# Proof of Work — банк доказів кандидата

Тут зібрані реальні артефакти досвіду. Кожен пункт можна показати рев’юеру.
Нічого вигаданого — лише те, що дійсно зроблено.

## Java і Spring Boot

- Capstone-сервіс на Spring Boot з REST API: каталог, замовлення, refund flow.
- Кілька issue-to-PR циклів із code review і merge у main.

## Бази даних

- PostgreSQL: схема БД, SQL-запити, Flyway-міграції в capstone-проєкті.

## Git і workflow

- Постійна робота з гілками та pull request, участь у code review.

## Тестування

- Unit-тести на JUnit для бізнес-правил.
- Integration-тести писалися обмежено (один модуль).

## Інфраструктура

- Docker: Dockerfile і docker-compose для локального запуску capstone-сервісу.
- GitHub Actions: базовий PR-check workflow (build + test).

## Прогалини (чесно)

- Комерційний стаж на Java: близько 1.5 року (нижче планки у 2 роки).
- Черги повідомлень (Kafka/RabbitMQ): лише теорія, без бойового досвіду.
- Метрики/observability: налаштовував лише логування.