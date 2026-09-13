# Discovery report: CashFlow MRR

## Top-level systems

Огляд обмежено каталогом цього завдання. У ньому присутні такі системи та артефакти:

- **MRR domain:** Java-пакет `com.acme.cashflow.mrr` містить моделі `Subscription`, `MrrSnapshot`, enum-и статусу/періоду та сервіси розрахунку ([Subscription.java](../src/main/java/com/acme/cashflow/mrr/Subscription.java#L5-L13), [MrrSnapshot.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshot.java#L3-L7), [BillingPeriod.java](../src/main/java/com/acme/cashflow/mrr/BillingPeriod.java#L3-L7), [SubscriptionStatus.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionStatus.java#L3-L9)).
- **Scheduled snapshot:** `MrrSnapshotJob` є запланованою точкою входу для щоденного snapshot ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L9-L14)).
- **Persistence contracts:** `SubscriptionRepository` і `MrrSnapshotRepository` представлені інтерфейсами; у межах каталогу concrete implementations не знайдені ([SubscriptionRepository.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionRepository.java#L5-L7), [MrrSnapshotRepository.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotRepository.java#L6-L10)).
- **Database schema:** дві SQL-міграції створюють `subscriptions` і додають `paused_at` ([V1__create_subscriptions.sql](../db/migration/V1__create_subscriptions.sql#L1-L11), [V2__add_paused_at.sql](../db/migration/V2__add_paused_at.sql#L1-L3)).
- **Runtime configuration:** PostgreSQL datasource, `ddl-auto: validate`, cron і прапорець paused-політики описані в YAML ([application.yml](../src/main/resources/application.yml#L1-L15)).
- **Build/test system:** Gradle використовує Spring Boot, JPA, PostgreSQL driver і JUnit Platform ([build.gradle](../build.gradle#L1-L23)).
- **Incident/discovery tooling:** бізнес-контекст міститься в [inputs/incident.md](../inputs/incident.md#L1-L19), а структурний огляд — у [scripts/discovery-scan.sh](../scripts/discovery-scan.sh#L1-L43).

## Critical runtime flows

### Щоденний snapshot

```text
@Scheduled 02:00
  -> LocalDate.now()
  -> SubscriptionService.findActiveSubscriptions(date)
       -> SubscriptionRepository.findAll()
       -> фільтр статусу та startedOn у пам'яті
  -> MrrCalculator.totalMrrCents(active, date)
       -> MONTHLY: priceCents
       -> YEARLY: priceCents / 12
  -> MrrSnapshotRepository.save(snapshot)
```

- `runDailySnapshot()` запускається cron-виразом `0 0 2 * * *` ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L28-L30)).
- Дата визначається через `LocalDate.now()`, тому залежить від timezone JVM/сервера ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L30-L30)).
- Job послідовно отримує підписки, обчислює суму та зберігає snapshot без видимої обробки помилок або часткових результатів ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L31-L34)).

### Вибірка активних підписок

- Сервіс викликає `findAll()` і фільтрує весь список у пам'яті ([SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L23-L27), [SubscriptionRepository.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionRepository.java#L5-L7)).
- `CANCELLED` відкидається; дата `startedOn` має бути не пізнішою за дату snapshot ([SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L29-L35)).
- `PAUSED` не відкидається: код прямо фіксує його як історично активний статус ([SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L32-L35)). `TRIAL` також проходить фільтр, оскільки окремого правила для нього немає ([SubscriptionStatus.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionStatus.java#L3-L9)).

### Нормалізація MRR

- `MrrCalculator` підсумовує нормалізоване значення для кожної переданої підписки ([MrrCalculator.java](../src/main/java/com/acme/cashflow/mrr/MrrCalculator.java#L15-L23)).
- `MONTHLY` повертає повну ціну, а `YEARLY` використовує цілочисельне ділення `priceCents() / 12` ([MrrCalculator.java](../src/main/java/com/acme/cashflow/mrr/MrrCalculator.java#L26-L37)).
- Аргумент `on` у калькуляторі не використовується; датова фільтрація відбувається раніше в `SubscriptionService` ([MrrCalculator.java](../src/main/java/com/acme/cashflow/mrr/MrrCalculator.java#L17-L23), [SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L23-L35)).

### Збереження snapshot

- Результат передається як `new MrrSnapshot(today, totalMrrCents)` у `MrrSnapshotRepository.save()` ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L31-L34)).
- У доступному коді не видно політики повторного запуску, унікальності дати snapshot або транзакційної межі ([MrrSnapshotRepository.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotRepository.java#L6-L10)).

## Change-risk areas

- **Paused-status semantics:** інцидент вказує, що Commerce OS виключає `PAUSED`, тоді як локальний фільтр виключає лише `CANCELLED` ([inputs/incident.md](../inputs/incident.md#L7-L10), [SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L29-L35)). Будь-яка зміна трактування статусів вплине на історичні та майбутні MRR snapshots.
- **Yearly arithmetic:** для річних планів залишок від ділення на 12 втрачається ([MrrCalculator.java](../src/main/java/com/acme/cashflow/mrr/MrrCalculator.java#L26-L37)). Правило округлення цільової системи в репозиторії не зафіксоване; зміна арифметики може змінити всі результати для `YEARLY`.
- **Config/code divergence:** YAML містить `cashflow.mrr.snapshot-cron` і `cashflow.mrr.include-paused-in-mrr`, але показаний job має hardcoded cron, а service не читає paused-прапорець ([application.yml](../src/main/resources/application.yml#L12-L15), [MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L28-L30), [SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L29-L35)). Налаштування та фактична поведінка можуть розходитись.
- **Date/time boundary:** `LocalDate.now()` без явної timezone робить результат залежним від середовища запуску ([MrrSnapshotJob.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L30-L30)).
- **Schema/domain mismatch:** SQL додає `paused_at`, але `Subscription` не містить `pausedAt` ([V2__add_paused_at.sql](../db/migration/V2__add_paused_at.sql#L1-L3), [Subscription.java](../src/main/java/com/acme/cashflow/mrr/Subscription.java#L5-L13)). Через показану модель час паузи не бере участі у визначенні активності.
- **Persistence and startup wiring:** concrete repository implementations, JPA entities, `@SpringBootApplication` і `@EnableScheduling` у доступному каталозі не знайдені. Це робить runtime-залежності неповністю визначеними ([SubscriptionRepository.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionRepository.java#L5-L7), [MrrSnapshotRepository.java](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotRepository.java#L6-L10)).
- **Data volume:** `findAll()` завантажує всі підписки перед фільтрацією ([SubscriptionService.java](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L23-L26)). Поведінка залежить від обсягу таблиці та реалізації repository.
- **Database startup assumptions:** datasource спрямований на локальний PostgreSQL, пароль читається з `${DB_PASSWORD}`, а Hibernate валідовує схему ([application.yml](../src/main/resources/application.yml#L4-L10)). SQL-міграції присутні, але у `build.gradle` не видно Flyway/Liquibase dependency ([build.gradle](../build.gradle#L14-L19)).

## Existing tests

- У каталозі є один тест: `MrrSnapshotIT` ([MrrSnapshotIT.java](../src/test/java/com/acme/cashflow/mrr/MrrSnapshotIT.java#L1-L29)).
- Тест запускає `@SpringBootTest`, викликає `mrrSnapshotJob.runDailySnapshot()` і перевіряє лише наявність snapshot за поточну дату ([MrrSnapshotIT.java](../src/test/java/com/acme/cashflow/mrr/MrrSnapshotIT.java#L16-L29)).
- Коментар тесту явно зазначає, що `YEARLY`-нормалізація та поведінка `PAUSED` не покриті ([MrrSnapshotIT.java](../src/test/java/com/acme/cashflow/mrr/MrrSnapshotIT.java#L10-L14)).
- У тесті немає перевірки точного `totalMrrCents`, межі `startedOn`, `TRIAL`, `CANCELLED`, дробового залишку для річної ціни, часової зони або повторного запуску за ту саму дату ([MrrSnapshotIT.java](../src/test/java/com/acme/cashflow/mrr/MrrSnapshotIT.java#L25-L29)).
- Через відсутність видимих application/repository beans у scope фактична працездатність `@SpringBootTest` залежить від компонентів поза каталогом або від неповноти fixture.

## Unclear assumptions

- Яке правило округлення `YEARLY` використовує Commerce OS: округлення для кожної підписки чи після підсумовування, і який режим округлення?
- Чи треба вважати кожен `PAUSED` запис неактивним, чи активність залежить від моменту `paused_at` відносно дати snapshot?
- Чи входять `TRIAL` підписки до MRR у цільовій системі?
- Яка timezone є контрактною для щоденного snapshot? Чи збігається вона з timezone сервера?
- Чи читається `paused_at` через зовнішній adapter, якого немає в цьому каталозі?
- Де знаходяться concrete implementations repository, JPA mapping, Spring application class і scheduler enablement, якщо вони є поза discovery scope?
- Як міграції з `db/migration` запускаються, якщо у видимому `build.gradle` немає Flyway/Liquibase dependency?
- Який owner затверджує семантику фінансового розрахунку? У доступних README та incident-файлі owner не вказаний ([README.md](../README.md#L12-L19), [inputs/incident.md](../inputs/incident.md#L12-L15)).
- Яка поведінка очікується при повторному запуску job для тієї самої дати: insert, update чи idempotent no-op?
- Чи є production-дані або зовнішній контракт, що пояснюють заявлену в incident.md розбіжність 6–9% ([inputs/incident.md](../inputs/incident.md#L3-L5))?

## Evidence

| Факт | Джерело |
|---|---|
| Інцидент: CashFlow MRR на 6–9% вищий, розбіжність зростає в місяцях із багатьма річними планами | [inputs/incident.md#L1-L8](../inputs/incident.md#L1-L8) |
| Commerce OS не включає `PAUSED` до MRR | [inputs/incident.md#L8-L10](../inputs/incident.md#L8-L10) |
| Discovery-обмеження забороняють зміну product code і конфігурації | [inputs/incident.md#L17-L19](../inputs/incident.md#L17-L19) |
| Job запускається о 02:00, дата береться з `LocalDate.now()` | [MrrSnapshotJob.java#L28-L34](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotJob.java#L28-L34) |
| `PAUSED` проходить фільтр, бо відкидається лише `CANCELLED` | [SubscriptionService.java#L23-L35](../src/main/java/com/acme/cashflow/mrr/SubscriptionService.java#L23-L35) |
| `YEARLY` нормалізується через цілочисельне `/ 12` | [MrrCalculator.java#L26-L37](../src/main/java/com/acme/cashflow/mrr/MrrCalculator.java#L26-L37) |
| YAML містить cron і paused-прапорець | [application.yml#L12-L15](../src/main/resources/application.yml#L12-L15) |
| `paused_at` є в SQL, але відсутній у `Subscription` | [V2__add_paused_at.sql#L1-L3](../db/migration/V2__add_paused_at.sql#L1-L3); [Subscription.java#L5-L13](../src/main/java/com/acme/cashflow/mrr/Subscription.java#L5-L13) |
| Є лише happy-path інтеграційний тест; `YEARLY` і `PAUSED` ним не покриті | [MrrSnapshotIT.java#L10-L29](../src/test/java/com/acme/cashflow/mrr/MrrSnapshotIT.java#L10-L29) |
| Видимі repository contracts — лише інтерфейси | [SubscriptionRepository.java#L5-L7](../src/main/java/com/acme/cashflow/mrr/SubscriptionRepository.java#L5-L7); [MrrSnapshotRepository.java#L6-L10](../src/main/java/com/acme/cashflow/mrr/MrrSnapshotRepository.java#L6-L10) |
| Збірка використовує Spring Boot/JPA/PostgreSQL та JUnit Platform | [build.gradle#L1-L23](../build.gradle#L1-L23) |
| Discovery-скрипт перелічує конфіги, тести та SQL-міграції у визначених шляхах | [scripts/discovery-scan.sh#L21-L43](../scripts/discovery-scan.sh#L21-L43) |