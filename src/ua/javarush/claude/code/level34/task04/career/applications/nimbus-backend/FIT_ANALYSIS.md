# Fit Analysis — Backend Engineer (Java) @ Nimbus Payments

## Must-have match

| Must-have вимога | Статус | Обґрунтування |
|---|---|---|
| Комерційний досвід на Java від 2 років | **Gap** | У `evidence/PROOF_OF_WORK.md` вказано близько 1,5 року комерційного досвіду — це нижче порогу вакансії. |
| Spring Boot і побудова REST API | **Matched** | Capstone-сервіс реалізований на Java/Spring Boot і має REST API для каталогу, замовлень та refund flow. |
| PostgreSQL та впевнений SQL | **Matched** | Є схема PostgreSQL, SQL-запити та Flyway-міграції в capstone-проєкті. |
| Git і pull request workflow | **Matched** | Підтверджено роботу з гілками, pull request, code review та merge в `main`. |
| Unit- та integration-тести на JUnit | **Partial match** | Є JUnit unit-тести бізнес-правил і один integration-тест на refund flow; integration-покриття обмежене одним модулем. |

Загалом 3 з 5 must-have підтверджені, один частково підтверджений, а вимога щодо комерційного стажу не досягнута. Це не повний must-have match, але технічний стек і спосіб роботи переважно відповідають вакансії.

## Nice-to-have match

| Nice-to-have вимога | Статус | Обґрунтування |
|---|---|---|
| Docker і контейнеризація | **Matched** | Є `Dockerfile` і `docker-compose` для локального запуску capstone-сервісу. |
| CI/CD, наприклад GitHub Actions | **Matched** | Наявний базовий GitHub Actions PR-check: build + test. |
| Черги повідомлень Kafka або RabbitMQ | **Gap** | У доказах зазначено лише теоретичне знання, без практичного або бойового досвіду. |
| Спостережуваність: логування та метрики | **Partial match** | Налаштовувалося логування, але метрики та моніторинг не налаштовувалися. |
| Зрозуміла технічна документація | **Partial match** | Є структуровані описи вакансії та capstone, але окремого production-прикладу технічної документації в evidence не наведено. |

## Matched evidence

- `evidence/PROOF_OF_WORK.md` — описує Spring Boot REST API, PostgreSQL, SQL, Flyway, Git-гілки, pull request, code review, merge, JUnit unit-тести, Docker, `docker-compose` і GitHub Actions PR-check.
- `evidence/capstone-summary.md` — конкретизує capstone-платіжний backend на Java/Spring Boot: REST API для каталогу, замовлень і refund flow; PostgreSQL + Flyway; JUnit unit-тести та один integration-тест; Docker/docker-compose; GitHub Actions build + test.
- `evidence/capstone-summary.md` — підтверджує issue-to-PR цикл із code review та доведення фічі від розбору задачі до merge.
- `evidence/PROOF_OF_WORK.md` — прямо фіксує обмеження: близько 1,5 року комерційного Java-досвіду, відсутність Kafka/RabbitMQ та наявність лише логування без метрик.

## Gaps

- Комерційний Java-досвід становить близько 1,5 року замість необхідних 2 років.
- Integration-тестування підтверджене лише одним модулем; немає широкого набору integration-тестів для ключових backend-сценаріїв.
- Практичний досвід Kafka або RabbitMQ відсутній.
- Метрики та повноцінний monitoring/observability не підтверджені; є тільки логування.
- Окремий production-рівень технічної документації серед наданих артефактів не підтверджений.

## Decision

**tailor-and-apply**

Рішення спирається на must-have match: Spring Boot/REST, PostgreSQL/SQL і Git/PR workflow повністю підтверджені, JUnit підтверджений частково, але формальна вимога щодо 2 років комерційного досвіду не виконана. Водночас це не підстава для `skip`, оскільки основний технічний стек і ключовий issue-to-PR процес відповідають вакансії. Перед поданням варто адаптувати резюме під платіжний refund flow, чітко показати наявний integration-тест і не маскувати 1,5 року стажу; паралельно посилити integration-покриття та observability evidence.

## Interview risks

- Рекрутер або hiring manager може відсіяти кандидатуру через невідповідність формальному порогу «від 2 років».
- На технічній співбесіді можуть попросити детально пояснити integration-тестування; одного тесту може бути недостатньо для очікуваного рівня самостійності.
- Платіжний домен підвищує вимоги до edge cases і надійності refund flow, тому потрібно бути готовими пояснити перевірки помилок, ідемпотентність і транзакційні межі лише в межах того, що реально зроблено.
- Можуть перевірити практичне застосування Kafka/RabbitMQ та метрик, яких у evidence немає; не слід подавати теоретичні знання як production-досвід.