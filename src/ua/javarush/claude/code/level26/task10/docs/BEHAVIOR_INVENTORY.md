# BEHAVIOR_INVENTORY.md — поточна поведінка billing flows

Документ фіксує факти з product code, tests та `RISK_MAP.md`. Він не описує бажану поведінку і не містить test code.

## Flow 1: Pause/resume

### Current behavior

`SubscriptionService.pause` встановлює для підписки статус `PAUSED` і записує `pausedAt`. `SubscriptionService.resume` встановлює статус `ACTIVE` і записує `resumedAt`. Ціна підписки цими операціями не змінюється. `MrrFormulas` включає до active set і `ACTIVE`, і `PAUSED`, тому paused-підписка продовжує додаватися до щоденного MRR-знімка.

### Inputs

- `Subscription sub`.
- `LocalDate day` для `pause` або `resume`.
- Поточний статус і `monthlyPriceCents` підписки.

### Outputs

- `pause` і `resume` мають тип `void`.
- Після `pause`: статус `PAUSED`, `pausedAt` дорівнює переданому `day`.
- Після `resume`: статус `ACTIVE`, `resumedAt` дорівнює переданому `day`.
- Окремий виклик `MrrFormulas.computeDailySnapshot` додає `monthlyPriceCents` paused-підписки до total.

### Edge cases

- Перевірки попереднього статусу немає: `pause` і `resume` просто встановлюють відповідний статус.
- Повторні `pause` або `resume` повторно записують відповідну дату.
- `CANCELLED` не входить до active set; `PAUSED` входить.
- У `Subscription` немає getters для `pausedAt` і `resumedAt`.

### Existing tests

У цій робочій директорії немає тесту для `pause`, `resume` або MRR-знімка paused-підписки. Наявні тестові файли — `PaymentRetryServiceTest` і `RefundServiceTest`.

### Missing checks

- Немає перевірки переходів статусів pause/resume.
- Немає перевірки того, як pause впливає на щоденний MRR-знімок.
- Немає перевірки повторних операцій і дат паузи/відновлення через публічні getters.

### Characterization candidate

yes — фактичне включення `PAUSED` до active set не має тестового покриття та суперечить опису в `BILLING_RULES.md`; це зона високого change risk.

### Evidence

- `src/main/java/cashflow/subscription/SubscriptionService.java:19-30` — зміна статусів і дат.
- `src/main/java/cashflow/subscription/MrrFormulas.java:7-23` — підрахунок snapshot та active set.
- `src/main/java/cashflow/subscription/Subscription.java:4-20` — поля підписки та відсутність date getters.
- `src/main/java/cashflow/subscription/SubscriptionStatus.java:2-4` — значення статусів.
- `docs/RISK_MAP.md:4-11` — risk area `mrr-engine / pause-resume`.
- `docs/BILLING_RULES.md:5-10` — задокументоване протилежне правило для `PAUSED`.

## Flow 2: Partial refund

### Current behavior

`SubscriptionService.refund` делегує операцію в `RefundService.applyPartialRefund` і повертає його результат. `RefundService` віднімає `amountCents` від поточної `monthlyPriceCents`, замінює від’ємний залишок на `0`, записує залишок у підписку та повертає початковий запитаний `amountCents`. Аргумент `day` у розрахунку не використовується.

### Inputs

- `Subscription sub`.
- `long amountCents`.
- `LocalDate day`.
- Поточне значення `sub.monthlyPriceCents`.

### Outputs

- `SubscriptionService.refund` повертає `long applied`.
- `sub.monthlyPriceCents` стає `max(previousMonthlyPriceCents - amountCents, 0)`.
- Повертається початковий `amountCents`, а не обов’язково фактично зменшена сума.

### Edge cases

- `amountCents == 0`: ціна не змінюється, повертається `0`.
- `amountCents` дорівнює поточній ціні: ціна стає `0`, повертається поточна ціна.
- `amountCents` більший за поточну ціну: ціна стає `0`, повертається вся запитана сума.
- Від’ємний `amountCents` збільшує ціну та повертається як від’ємне значення.
- `day` не впливає на результат, зокрема для дня білінгу.
- Повторний refund обчислюється від уже зміненої ціни.

### Existing tests

`RefundServiceTest.partialRefundReducesPrice` — єдиний тест refund; він перевіряє refund `1500` для ціни `5000`, результат `1500` і нову ціну `3500`. Тестового коду в цьому документі немає.

### Missing checks

- Немає тесту для refund у день білінгу.
- Немає тесту для refund, більшого за залишок.
- Немає тестів для нульового або від’ємного `amountCents`.
- Немає перевірки поведінки повторного refund.

### Characterization candidate

yes — наявний лише один happy-path тест, а карта ризиків прямо називає billing-day і over-refund edge cases непокритими.

### Evidence

- `src/main/java/cashflow/subscription/SubscriptionService.java:32-36` — делегування та результат.
- `src/main/java/cashflow/subscription/RefundService.java:9-18` — арифметика, clamp до нуля та повернене значення.
- `src/test/java/cashflow/subscription/RefundServiceTest.java:6-19` — наявний happy-path тест.
- `docs/RISK_MAP.md:19-25` — risk area `refunds / proration` і непокриті edge cases.

## Flow 3: Failed payment → retry success

### Current behavior

`PaymentRetryService.retry` виконує не більше `maxRetries` викликів `PaymentAttempt.charge`. `charge` збільшує `currentAttempt` і повертає успіх, коли лічильник досягає `succeedOnAttempt`. Після першого успішного `charge` retry викликає `markResolved` і повертає `true`. Якщо всі дозволені спроби невдалі, повертається `false`, а `resolved` не змінюється. Churn, MRR, дата failed payment та інтервал до retry не зберігаються і не перераховуються.

### Inputs

- `PaymentAttempt attempt`.
- `int maxRetries` у `PaymentRetryService`.
- `int succeedOnAttempt` у `PaymentAttempt`.
- Поточний `currentAttempt` і `resolved`.

### Outputs

- `retry` повертає `boolean`.
- При успішній спробі повертається `true`, `resolved` встановлюється в `true`.
- Після вичерпання невдалих спроб повертається `false`, `resolved` залишається без зміни.
- `currentAttempt` збільшується кожним викликом `charge`.

### Edge cases

- `maxRetries <= 0`: спроби не виконуються, результат `false`.
- `succeedOnAttempt <= 1`: перша спроба успішна.
- `succeedOnAttempt > maxRetries`: один виклик `retry` повертає `false`.
- `currentAttempt` зберігається між викликами `retry`; наступний виклик продовжує лічильник.
- Перевірки `attempt.isResolved()` перед новим retry немає.
- Для `attempt == null` виклик `charge` призводить до `NullPointerException`.

### Existing tests

`PaymentRetryServiceTest.retrySucceedsOnSecondAttempt` створює `PaymentAttempt(2)` і `PaymentRetryService(3)` та перевіряє, що `retry` повертає `true`. Інших тестів retry у цій робочій директорії немає.

### Missing checks

- Немає перевірки `isResolved()` після успішного retry.
- Немає тесту остаточно невдалого retry та межі `maxRetries`.
- Немає тестів повторного виклику retry або значень `maxRetries <= 0`.
- Немає перевірки churn dip між failed payment і retry.

### Characterization candidate

yes — покрито лише happy path, тоді як ліміт спроб, стан resolved і продовження лічильника між викликами визначають окремі фактичні результати; це відповідає risk area з medium-high change risk.

### Evidence

- `src/main/java/cashflow/payment/PaymentAttempt.java:3-19` — стан спроби, `charge` і `resolved`.
- `src/main/java/cashflow/payment/PaymentRetryService.java:7-24` — цикл retry та результат.
- `src/test/java/cashflow/payment/PaymentRetryServiceTest.java:5-16` — єдиний happy-path тест.
- `docs/RISK_MAP.md:12-18` — risk area `payments / retry` і непокритий churn dip.
- `docs/ARCHITECTURE_CURRENT.md:10-15` — відсутність перерахунку churn між failed payment і retry.

## Flow 4: Plan switch on billing day

### Current behavior

`PlanSwitchService.switchPlan` обчислює `onBillingDay` через `switchDay.equals(billingDay)`, але не використовує це значення. Незалежно від збігу дат метод одразу встановлює `sub.monthlyPriceCents` у `newPriceCents`. Метод не виконує списання, proration або перевірку подвійного списання та має тип `void`.

### Inputs

- `Subscription sub`.
- `long newPriceCents`.
- `LocalDate billingDay`.
- `LocalDate switchDay`.
- Поточне значення `sub.monthlyPriceCents`.

### Outputs

- Метод повертає `void`.
- `sub.monthlyPriceCents` одразу дорівнює `newPriceCents`.
- Результат однаковий для збігу та незбігу `switchDay` і `billingDay`, якщо вхідні об’єкти не є `null`.
- Окреме списання або proration не створюється цим методом.

### Edge cases

- `switchDay == null`: виклик `switchDay.equals(billingDay)` призводить до `NullPointerException`.
- `billingDay == null` при ненульовому `switchDay`: `onBillingDay` стає `false`, ціна все одно змінюється.
- `sub == null`: setter призводить до `NullPointerException`.
- `newPriceCents` без перевірки передається setter-у, зокрема `0` або від’ємне значення.
- На billing day немає окремого шляху для списання, подвійного списання чи proration.

### Existing tests

У цій робочій директорії немає тесту для `PlanSwitchService` або `switchPlan`. Інші наявні тести — `PaymentRetryServiceTest` і `RefundServiceTest`.

### Missing checks

- Немає тесту збігу `switchDay` і `billingDay`.
- Немає тесту незбігу дат для порівняння поведінки.
- Немає перевірки, що на billing day не виконується proration або додаткове списання.
- Немає перевірки допустимості `newPriceCents`.

### Characterization candidate

no — окремої billing-day поведінки в коді немає: обчислений прапорець не використовується, а для обох дат виконується той самий setter; окремої plan-switch risk area в `RISK_MAP.md` також немає.

### Evidence

- `src/main/java/cashflow/subscription/PlanSwitchService.java:5-16` — невикористаний billing-day прапорець і безумовна заміна ціни.
- `src/test/java` — відсутній тест `PlanSwitchService`/`switchPlan`; наявні лише `PaymentRetryServiceTest` і `RefundServiceTest`.
- `docs/RISK_MAP.md:19-25` — найближча наявна risk area `refunds / proration`; карта не містить окремої області plan switch.