# Поточна архітектура billing-коду

## Scope

Документ описує фактичну реалізацію пакетів `subscription` і `mrr`, а також scheduled jobs, які їх з’єднують, у межах `task06`. Джерелом є код, тести та operational runbook; кожен істотний висновок має evidence anchor у форматі `file:line`. Історичний `docs/ARCHITECTURE.md` використовується лише для порівняння в окремому розділі й не вважається описом поточної поведінки.

У межі огляду входять:

- модель `Subscription`, її статуси та правило активності;
- in-memory API `SubscriptionService`;
- нормалізація сум і підрахунок MRR;
- `MrrSnapshotJob` і `SubscriptionExpiryJob`;
- увімкнення Spring scheduling.

Не входять до scope зміни product code або тестів, а також припущення про зовнішню інфраструктуру, яка не представлена в репозиторії.

## Confirmed facts

### Запуск і планувальники

- Точка входу — `BillingApplication`; Spring Boot запускається з `@SpringBootApplication`, а планувальники вмикаються через `@EnableScheduling` (`src/main/java/com/rush/billing/BillingApplication.java:6-8`).
- `MrrSnapshotJob` запускається щодня о 02:00 за cron `0 0 2 * * *` (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:25-32`). Він бере активні підписки, обчислює загальний MRR і друкує значення в stdout у форматі `[MrrSnapshotJob] daily MRR snapshot = ...` (`.../MrrSnapshotJob.java:30-32`).
- `SubscriptionExpiryJob` запускається з інтервалом 3 600 000 мс, тобто щогодини (`src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:19-26`). Він лише рахує кількість активних підписок і друкує її; статуси не змінює (`.../SubscriptionExpiryJob.java:20-26`).
- Snapshot не записується в БД або інше сховище: реалізація містить лише `System.out.println` (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:30-32`). Це також зафіксовано в runbook (`docs/RUNBOOK.md:10-13`).

### Модель підписки

`Subscription` зберігає `id`, `amount`, `billingPeriod`, `discountRate` і `status`; перші чотири поля оголошені `final`, а `status` — ні (`src/main/java/com/rush/billing/subscription/Subscription.java:9-26`). У класі немає дат початку/завершення або setter-методу для зміни статусу після створення (`.../Subscription.java:17-55`).

Можливі статуси — `ACTIVE`, `TRIAL`, `CANCELED`, `EXPIRED` (`src/main/java/com/rush/billing/subscription/SubscriptionStatus.java:6-11`). `isActive()` повертає `true` лише для `ACTIVE` або `TRIAL`, а `isTrial()` — лише для `TRIAL` (`src/main/java/com/rush/billing/subscription/Subscription.java:45-55`). Коментар enum прямо уточнює, що `TRIAL` активний для доступу, але не враховується в MRR (`src/main/java/com/rush/billing/subscription/SubscriptionStatus.java:3-5`).

### Сховище та `SubscriptionService`

- `SubscriptionService` є Spring `@Service` і зберігає підписки в `ConcurrentHashMap<String, Subscription>` (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:9-18`).
- Попри наявність `spring-boot-starter-data-jpa` серед залежностей (`build.gradle:14-18`), сервіс прямо описує своє сховище як in-memory і не містить JPA-репозиторію (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:12-13`).
- `save(subscription)` зберігає об’єкт за його `id`; повторне збереження того самого `id` замінює попереднє значення (`.../SubscriptionService.java:20-22`).
- `findActive()` перебирає значення map і повертає новий список для підписок, де `isActive()` істинний (`.../SubscriptionService.java:24-32`). Отже, він включає `ACTIVE` і `TRIAL`.
- `findAll()` повертає новий список усіх значень map без фільтрації (`.../SubscriptionService.java:34-36`). Порядок елементів не є контрактом, оскільки джерелом є `ConcurrentHashMap.values()`.
- Тест підтверджує, що `findActive()` включає `ACTIVE` і `TRIAL`, але не `CANCELED` (`src/test/java/com/rush/billing/subscription/SubscriptionServiceTest.java:11-21`).

### Розрахунок MRR

`MrrCalculationService.totalMrr(List<Subscription>)` починає з `BigDecimal.ZERO` і послідовно додає місячний еквівалент придатних підписок (`src/main/java/com/rush/billing/mrr/MrrCalculationService.java:23-39`). Фільтри застосовуються так:

1. неактивна підписка пропускається (`!s.isActive()`);
2. `TRIAL` пропускається окремо (`s.isTrial()`);
3. для решти викликається `MrrFormulas.monthlyAmount(s)`.

Практичний наслідок: до MRR входять лише підписки зі статусом `ACTIVE`. `TRIAL` потрапляє до `findActive()`, але вилучається MRR-сервісом; `CANCELED` і `EXPIRED` відкидаються перевіркою активності (`src/main/java/com/rush/billing/mrr/MrrCalculationService.java:27-37`). Тест підтверджує виключення trial-підписки (`src/test/java/com/rush/billing/mrr/MrrCalculationServiceTest.java:15-24`).

`MrrFormulas.monthlyAmount` нормалізує суму так (`src/main/java/com/rush/billing/mrr/MrrFormulas.java:16-27`):

- `MONTHLY` використовує `amount` без поділу;
- `YEARLY` ділить суму на 12 із масштабом 2 і `RoundingMode.HALF_UP`;
- після нормалізації обчислюється `1 - discountRate`;
- результат множення округлюється до 2 знаків через `HALF_UP`.

Отже, знижка застосовується **після** перетворення річної суми на місячну (`.../MrrFormulas.java:18-27`). Річний план `1200.00` без знижки дає `100.00`, що перевірено тестом (`src/test/java/com/rush/billing/mrr/MrrCalculationServiceTest.java:27-34`). Доступні періоди обмежені `MONTHLY` і `YEARLY` (`src/main/java/com/rush/billing/mrr/BillingPeriod.java:5-8`).

### Поточний runtime-потік

```text
SubscriptionService.store
        │
        ├─ findActive()  ── ACTIVE + TRIAL
        │
        ▼
MrrSnapshotJob.captureDailySnapshot()
        │
        ▼
MrrCalculationService.totalMrr()
        │  └─ відкидає TRIAL
        ▼
MrrFormulas.monthlyAmount()
        │  └─ YEARLY / 12 → discount → scale 2 HALF_UP
        ▼
stdout: daily MRR snapshot
```

Поточний production-код task06 не містить HTTP-виклику для цього розрахунку; єдиний знайдений виклик MRR-сервісу в застосунковому коді — щоденний job (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:16-32`).

## Assumptions

Ці пункти є обережними висновками з локального репозиторію, а не окремо підтвердженими вимогами бізнесу:

- Якщо не вказано інше, час `02:00` для cron інтерпретується планувальником у часовій зоні runtime; код не задає timezone (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:29`).
- `System.out.println` у scheduled jobs вважається операційним логом, хоча конкретна конфігурація збору stdout у deployment-коді відсутня (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:30-32`; `src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:23-26`).
- Дані `ConcurrentHashMap` вважаються процесними й ефемерними: після завершення JVM або перезапуску застосунку вони не відновлюються з БД, оскільки persistence-викликів у `SubscriptionService` немає (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:12-36`).
- Вважається, що коректні вхідні дані (`amount`, `discountRate`, список і його елементи) передаються викликачем, бо конструкторами та формулами не встановлено бізнес-валідацію (`src/main/java/com/rush/billing/subscription/Subscription.java:17-26`; `src/main/java/com/rush/billing/mrr/MrrFormulas.java:21-27`).

## What needs manual verification

- Перевірити в запущеному середовищі фактичну timezone для `MrrSnapshotJob`, бо cron не містить timezone-параметра (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:29`).
- Перевірити конфігурацію deployment/logging, щоб встановити, куди потрапляє stdout і чи зберігаються рядки snapshot поза процесом; у цьому task06 є лише `System.out.println` (`.../MrrSnapshotJob.java:30-32`; `docs/RUNBOOK.md:10-13`).
- Перевірити, чи існують зовнішні виклики `SubscriptionService.save()` або інше наповнення store поза переглянутими файлами; локальна реалізація не містить API завантаження підписок (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:18-36`).
- Перевірити поведінку на production-даних із `discountRate` поза діапазоном `[0, 1]`, від’ємними сумами та граничними значеннями округлення: код не валідовує ці параметри (`src/main/java/com/rush/billing/mrr/MrrFormulas.java:21-27`).
- Перевірити concurrency-властивості повного циклу `findActive()` → MRR при одночасних `save()`: map є concurrent, але окремий snapshot операцій не синхронізований (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:18-31`).

## Differences from historical documentation

Порівняння з твердженнями в `docs/ARCHITECTURE.md`:

- Історичний документ стверджує, що TRIAL включається в MRR як майбутній дохід (`docs/ARCHITECTURE.md:9-12`), але поточний код виключає `TRIAL` у `MrrCalculationService` (`src/main/java/com/rush/billing/mrr/MrrCalculationService.java:27-37`).
- Історичний документ описує застосування знижки до нормалізації річного плану (`docs/ARCHITECTURE.md:11-12`), тоді як поточна формула спочатку ділить YEARLY на 12, а потім застосовує знижку (`src/main/java/com/rush/billing/mrr/MrrFormulas.java:21-27`).
- Історичний документ описує обчислення на кожен HTTP-запит (`docs/ARCHITECTURE.md:13`), але в поточному коді MRR обчислюється щоденним scheduled job о 02:00 (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:25-32`); HTTP endpoint для цього потоку відсутній у переглянутому застосунковому коді.
- Історичний документ стверджує, що підписки зберігаються в PostgreSQL через JPA (`docs/ARCHITECTURE.md:17-18`), тоді як поточна реалізація використовує `ConcurrentHashMap` і прямо вказує на відсутність реальної БД (`src/main/java/com/rush/billing/subscription/SubscriptionService.java:12-18`).
- Історичний документ говорить про видалення прострочених підписок нічним batch-процесом (`docs/ARCHITECTURE.md:18-19`), але поточний `SubscriptionExpiryJob` запускається щогодини та лише читає/логгує кількість активних підписок (`src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:19-26`).
- Історичний документ описує один щотижневий scheduled job (`docs/ARCHITECTURE.md:22-23`), тоді як поточний код має окремі щоденний MRR snapshot і щогодинний expiry job (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:29-32`; `src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:23-26`).

## Додаткові обмеження реалізації

- У конструкторах і формулах немає явної валідації суми, періоду або ставки знижки (`src/main/java/com/rush/billing/subscription/Subscription.java:17-26`; `src/main/java/com/rush/billing/mrr/MrrFormulas.java:21-27`). Для `null` у списку, елементах, сумі або ставці знижки немає спеціальної обробки.
- `SubscriptionExpiryJob` не переводить підписки в `EXPIRED` і не видаляє їх; він лише читає кількість активних (`src/main/java/com/rush/billing/job/SubscriptionExpiryJob.java:20-26`).
- MRR snapshot не персистується, а лише виводиться у stdout (`src/main/java/com/rush/billing/job/MrrSnapshotJob.java:30-32`).