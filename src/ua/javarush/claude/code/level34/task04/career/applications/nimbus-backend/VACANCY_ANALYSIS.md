# Vacancy Analysis — Backend Engineer (Java) @ Nimbus Payments

Джерело: `jobs/nimbus-backend.md`. Нижче тільки те, що є в тексті вакансії;
власні інтерпретації винесені в `Hidden signals` і позначені як hypothesis.

## Must-have

- Комерційний досвід на Java від 2 років.
- Spring Boot і побудова REST API.
- Реляційні бази даних (PostgreSQL) та впевнений SQL.
- Git і pull request workflow: гілки, code review, merge.
- Unit- та integration-тести на JUnit.

## Nice-to-have

- Docker і контейнеризація сервісів.
- CI/CD, наприклад GitHub Actions.
- Черги повідомлень (Kafka або RabbitMQ).
- Спостережуваність: логування та метрики.
- Навичка писати зрозумілу технічну документацію.

## Responsibilities

- Розробка та підтримка backend-сервісів на Java і Spring Boot.
- Ведення задач в issue-to-PR циклі: розбір, реалізація, code review, merge.
- Покриття змін тестами та підтримка надійності платіжного flow.
- Участь у покращенні внутрішніх інструментів команди.

## Stack

- Мова: Java.
- Framework: Spring Boot, REST API.
- Сховище: PostgreSQL, SQL.
- Інструменти: Git, JUnit; опціонально Docker, GitHub Actions, Kafka/RabbitMQ.

## Seniority signals

- Планка стажу «від 2 років» і ownership за workstream → рівень middle.
- Вимога integration-тестів і code review → очікується самостійність у PR-циклі.

## Hidden signals

- (hypothesis) Платіжний домен (refund flow, підписки, білінг) → ймовірно важливі
  акуратність із грошима і тести на edge cases.
- (hypothesis) «Беруть ownership і швидко зростають» → можлива висока автономність та
  очікування зростання до senior.
- (hypothesis) «Покращення внутрішніх інструментів» → може цінуватися навичка писати
  скрипти/automation, хоча прямо у вимогах цього немає.

## Red flags

- Прямих red flags у тексті вакансії немає.
- (hypothesis) Формулювання «швидко зростають» може означати високе навантаження — варто
  уточнити на інтерв'ю.

## Evidence needed

Зіставлення вимог з артефактами кандидата з `evidence/PROOF_OF_WORK.md`:

- Java + Spring Boot, REST API → capstone-сервіс на Spring Boot.
- PostgreSQL + SQL → схема БД та Flyway-міграції в capstone.
- Git / PR workflow → issue-to-PR цикли з code review.
- Unit/integration-тести (JUnit) → unit-тести є; потрібен приклад integration-тесту.
- Docker, GitHub Actions → Dockerfile, docker-compose, PR-check workflow.
- Черги повідомлень → evidence відсутній (потрібно або добрати, або відзначити як gap).
