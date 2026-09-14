# Поточна архітектура CashFlow Dashboard

> Статус документа: read-only реконструкція фактичної реалізації за вихідним кодом у цьому завданні.
> Історичний опис у `docs/ARCHITECTURE.md` не змінюється.

## Межі та джерела

Опис охоплює код у `src/main/java`, конфігурацію збірки в `build.gradle`, а також операційні примітки в `docs/RUNBOOK.md`. Факти нижче прив'язані до конкретних файлів і рядків, щоб їх можна було повторно перевірити.

## Confirmed facts

### Запуск застосунку та планувальники

- Точкою входу є `com.rush.billing.BillingApplication`; клас позначений `@SpringBootApplication` і `@EnableScheduling` (`src/main/java/com/rush/billing/BillingApplication.java:6-8`).
- У коді є два Spring-компоненти з `@Scheduled`:
  - `MrrSnapshotJob` запускає `captureDailySnapshot()` за cron `0 0 2 * * *`, тобто щодня о 02:00 за часовою зоною runtime за замовчуванням (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:13-14,25-33`).
  - `SubscriptionExpiryJob` запускає `markExpired()` з `fixedRate = 3600000`, тобто з фіксованим інтервалом 3 600 000 мс (`src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:10-11,19-27`).
- `MrrSnapshotJob` отримує активні підписки через `SubscriptionService`, обчислює загальний MRR і виводить snapshot у `System.out`; запису в БД або інше сховище в цьому коді немає (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:25-33`).
- `SubscriptionExpiryJob` лише рахує кількість активних підписок і виводить її в `System.out`. Метод не змінює статуси підписок (`src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:19-27`).

### Підписки та їхнє сховище

- `SubscriptionService` є Spring `@Service` і зберігає підписки в `ConcurrentHashMap<String, Subscription>` (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:15-18`).
- `save()` кладе підписку в map за її ID; `findAll()` повертає копію значень, а `findActive()` повертає лише підписки, для яких `isActive()` істинний (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:20-36`).
- Реальної інтеграції з БД або `SubscriptionRepository` у переглянутому коді немає. Коментар самого сервісу прямо зазначає, що JPA є лише серед залежностей, а фактичне сховище — in-memory map (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:11-13`).
- `Subscription` містить незмінні після створення `id`, `amount`, `billingPeriod`, `discountRate` та змінюваний через внутрішнє поле `status`; публічного setter або іншого методу зміни статусу немає (`src/main/java/com/rush/billing/subscription/Subscription.java:9-26,29-47`).
- Активними для `SubscriptionService.findActive()` вважаються тільки статуси `ACTIVE` і `TRIAL`; `CANCELED` та `EXPIRED` неактивні (`src/main/java/com/rush/billing/subscription/Subscription.java:49-55`). Перелік статусів визначено в `SubscriptionStatus` (`src/main/java/com/rush/billing/subscription/SubscriptionStatus.java:6-11`).

### MRR engine

- `MrrCalculationService.totalMrr()` проходить переданий список, пропускає неактивні підписки та окремо пропускає `TRIAL`; до суми потрапляють лише активні підписки зі статусом `ACTIVE` (`src/main/java/com/rush/billing/mrr/MrrCalculationService.java:23-39`).
- Нормалізація суми виконується в `MrrFormulas.monthlyAmount()`:
  1. місячний план використовує `amount` без поділу;
  2. річний план ділиться на 12 із точністю 2 знаки та `RoundingMode.HALF_UP`;
  3. після нормалізації застосовується множник `1 - discountRate`;
  4. результат округлюється до 2 знаків через `HALF_UP` (`src/main/java/com/rush/billing/mrr/MrrFormulas.java:21-28`).
- Snapshot передає в MRR engine результат `findActive()`. Додаткова перевірка `TRIAL` у `totalMrr()` потрібна для коректності, якщо сервіс викликають безпосередньо зі списком, що містить trial-підписки (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:30-32`, `src/main/java/com/rush/billing/mrr/MrrCalculationService.java:27-37`).
- У переглянутому дереві `src/main/java` немає HTTP controller або dashboard endpoint, який викликав би MRR на кожен HTTP-запит.

### Збірка та залежності

- Проєкт використовує Java plugin, Spring Boot `2.7.18` і Spring Dependency Management `1.0.15.RELEASE` (`build.gradle:1-3`).
- Вказано `sourceCompatibility = '11'`; версія проєкту — `1.4.2-LEGACY` (`build.gradle:6-8`).
- Залежності: Spring Web, Spring Data JPA, runtime H2 та Spring Boot Test (`build.gradle:14-19`). Наявність `spring-boot-starter-data-jpa` і H2 сама по собі не означає, що підписки фактично зберігаються в БД.
- Для локального запуску `docs/RUNBOOK.md` пропонує `./gradlew bootRun` (`docs/RUNBOOK.md:4-8`). У поточній директорії Gradle wrapper `gradlew` відсутній.

## Assumptions

Нижче наведені твердження, які не випливають однозначно з переглянутого коду і тому не використовуються як підтверджені властивості системи:

- Час `02:00` для `MrrSnapshotJob` інтерпретується часовою зоною JVM/середовища, оскільки в коді не задано `zone` і в дереві немає application-конфігурації, що її перевизначає.
- `fixedRate` планувальника означає запуск із періодом від початку попереднього запуску; фактичну поведінку при довгому або паралельному виконанні не слід вважати встановленою без runtime-перевірки конфігурації scheduler-пулу.
- H2 може бути автоматично підхоплений Spring Boot, якщо в runtime немає інших datasource-налаштувань, але цей механізм не є шляхом збереження підписок у поточній реалізації.
- Заявлені в історичній документації плани щодо Redis і винесення MRR в окремий мікросервіс не можна вважати реалізованими або скасованими лише на підставі цього дерева; у поточному коді їхніх компонентів не знайдено.

## What needs manual verification

Перевірки, які потрібні в середовищі запуску або залишилися непідтвердженими статичним оглядом:

- Запустити застосунок через рекомендовану команду `./gradlew bootRun` у середовищі, де присутній wrapper, і перевірити успішний старт Spring context.
- Перевірити фактичну часову зону, час першого запуску daily snapshot і поведінку `fixedRate` у runtime.
- Перевірити, що daily snapshot і hourly expiry-check з'являються саме в очікуваному stdout/log pipeline, а snapshot не записується в durable storage.
- Перевірити сценарій із `ACTIVE`, `TRIAL`, `CANCELED` та `EXPIRED`: `TRIAL` має залишатися доступним як active для `findActive()`, але не входити до MRR; неактивні статуси не мають входити до MRR.
- Перевірити повторний виклик `save()` з тим самим ID і поведінку конкурентних операцій, якщо це має значення для deployment: map зберігає останнє значення за ключем, але API не визначає бізнес-правила конфліктів.
- Автоматичний запуск тестів у цій директорії не завершився успішно: `./gradlew test` неможливо виконати через відсутній wrapper; системна команда `gradle test` зупинилася під час оцінювання `build.gradle` на `sourceCompatibility` (`Could not set unknown property 'sourceCompatibility' for root project`). Це обмеження перевірки середовища, а не висновок про поведінку product code.

## Differences from historical documentation

| Тема | Стара документація (`docs/ARCHITECTURE.md`) | Фактична реалізація | Статус |
|---|---|---|---|
| Включення trial у MRR | Усі підписки, включно з `TRIAL`, враховуються (`:11`). | `MrrCalculationService` пропускає `TRIAL`; у MRR входять лише активні non-trial підписки (`MrrCalculationService.java:27-39`). | Підтверджена розбіжність |
| Порядок знижки | Знижка застосовується до нормалізації річного плану (`:12`). | Річна сума спочатку ділиться на 12, потім застосовується знижка (`MrrFormulas.java:21-28`). | Підтверджена розбіжність |
| Момент розрахунку | Розрахунок відбувається на кожен HTTP-запит dashboard (`:13`). | У переглянутому application code немає controller/dashboard endpoint; MRR явно обчислюється daily scheduled job (`MrrSnapshotJob.java:25-33`). | Підтверджена розбіжність у межах дерева |
| Сховище підписок | PostgreSQL через JPA `SubscriptionRepository` (`:17`). | `ConcurrentHashMap` у `SubscriptionService`; repository не використовується (`SubscriptionService.java:11-18`). | Підтверджена розбіжність |
| Завершення підписок | Expired підписки видаляються нічним batch-процесом (`:18`). | `SubscriptionExpiryJob` працює щогодини, лише рахує active і не змінює статус та не видаляє записи (`SubscriptionExpiryJob.java:19-27`). | Підтверджена розбіжність |
| Scheduling | Один weekly job перераховує аналітику (`:22`). | Два jobs: daily MRR snapshot о 02:00 і hourly expiry-check (`MrrSnapshotJob.java:25-33`, `SubscriptionExpiryJob.java:19-27`). | Підтверджена розбіжність |
| Результат snapshot | Стара архітектурна документація не фіксує спосіб збереження snapshot. | Поточний runbook підтверджує: snapshot лише пишеться в лог і не зберігається (`docs/RUNBOOK.md:10-13`). | Уточнення поточної реалізації |
| Майбутні плани | Redis-кеш і окремий MRR microservice (`:24-27`). | У поточному дереві відповідних компонентів немає. | Не підтверджено як реалізоване; потребує окремої перевірки scope |

## Потік даних

1. `SubscriptionService.save()` додає об'єкт `Subscription` до in-memory map.
2. Планувальник або інший caller отримує активні записи через `findActive()`.
3. `MrrSnapshotJob` передає список до `MrrCalculationService.totalMrr()`.
4. MRR engine відкидає неактивні та trial-підписки, нормалізує суми через `MrrFormulas` і підсумовує `BigDecimal`.
5. `MrrSnapshotJob` виводить отримане значення в stdout; durable persistence для snapshot у поточній реалізації відсутня.
6. Окремий `SubscriptionExpiryJob` не виконує mutation: він лише перевіряє кількість активних записів і логгує результат.