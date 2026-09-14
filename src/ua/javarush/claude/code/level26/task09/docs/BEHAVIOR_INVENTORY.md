# Інвентар поведінки CashFlow Dashboard

Фактична поведінка за кодом, тестами та наявними документами. `BILLING_RULES.md`
вважається описом очікуваного правила, якщо він розходиться з кодом.

## Flow 1: pause → resume підписки

**Current behavior**

`SubscriptionService.pause` встановлює статус `PAUSED` і записує дату паузи.
Попри паузу, `MrrFormulas.isInActiveSet` залишає підписку в active set, тому її
місячна сума входить до щоденного MRR. `SubscriptionService.resume` встановлює
статус `ACTIVE` і записує дату відновлення; суму MRR окремо не перераховує.

**Inputs**

- `Subscription` зі статусом `ACTIVE` та його `monthlyPriceCents`.
- Дата, передана до `pause`.
- Дата, передана до `resume`.
- Набір підписок, переданий до `computeDailySnapshot`.

**Outputs**

- Після pause: статус `PAUSED`, записана дата паузи, підписка все ще врахована
  в active MRR.
- Після resume: статус `ACTIVE`, записана дата відновлення, та сама сума MRR у
  наступному знімку.

**Existing tests**

Немає тестів на pause/resume або на включення `PAUSED` у щоденний MRR; це
зазначено в `RISK_MAP.md`.

**Missing checks**

- Перевірити статус і дату після pause.
- Перевірити, чи входить `PAUSED` до active MRR у щоденному знімку.
- Перевірити статус і дату після resume та незмінність суми MRR.
- Зафіксувати розбіжність із правилом про виключення `PAUSED` з active MRR.

**Characterization candidate**

yes — зафіксувати фактичний результат pause/resume та поточне включення
`PAUSED` до active MRR без зміни product code.

**Evidence**

- Code: `src/main/java/cashflow/subscription/SubscriptionService.java:21-30`
- Code: `src/main/java/cashflow/subscription/MrrFormulas.java:8-23`
- Code: `src/main/java/cashflow/subscription/SubscriptionStatus.java:3`
- Tests: `RISK_MAP.md` вказує на відсутність тестів у зоні pause/resume.
- Docs: `docs/BILLING_RULES.md:5-10` описує протилежне правило.
- Risk map: `docs/RISK_MAP.md:4-10`

## Flow 2: failed payment → retry success

**Current behavior**

`PaymentRetryService.retry` виконує не більше `maxRetries` послідовних спроб.
Після першої успішної `charge()` він викликає `markResolved`, негайно повертає
`true` і не виконує подальші спроби. Якщо всі спроби невдалі, повертає `false` і
не позначає attempt як resolved. Churn dip між failed payment і успішним retry
не перераховується.

**Inputs**

- `PaymentAttempt`, який визначає результат кожної `charge()` через
  `succeedOnAttempt`.
- `maxRetries` у `PaymentRetryService`.
- Послідовність результатів `charge()` — невдалі спроби до першої успішної.

**Outputs**

- При успіху в межах ліміту: `true`, attempt позначено як resolved, подальші
  спроби припинено.
- При вичерпанні ліміту: `false`, attempt не позначено як resolved.
- Окремий churn dip між failed payment і retry відсутній у перерахунку.

**Existing tests**

Є один happy-path тест: `PaymentRetryServiceTest.retrySucceedsOnSecondAttempt`
перевіряє повернення `true` для успіху на другій спробі, але не перевіряє
resolved-стан або churn dip.

**Missing checks**

- Перевірити, що успішний retry позначає attempt як resolved.
- Перевірити припинення спроб після першого успіху.
- Перевірити `false` і невирішений стан після вичерпання `maxRetries`.
- Перевірити відсутність churn dip між failed payment та retry success.

**Characterization candidate**

yes — зафіксувати поточні результати retry для успіху на різній спробі та для
вичерпання ліміту без зміни product code.

**Evidence**

- Code: `src/main/java/cashflow/payment/PaymentRetryService.java:16-24`
- Code: `src/main/java/cashflow/payment/PaymentAttempt.java:2-18`
- Tests: `src/test/java/cashflow/payment/PaymentRetryServiceTest.java:5-16`
- Docs: `docs/ARCHITECTURE_CURRENT.md:10-14`
- Risk map: `docs/RISK_MAP.md:12-17`