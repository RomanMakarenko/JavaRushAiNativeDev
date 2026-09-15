# Risk map — модуль розрахунку MRR (`com.acme.cashflow.mrr`)

> Legacy-модуль розрахунку Monthly Recurring Revenue для білінгу ACME Cashflow.
> Карта ризиків зібрана за фактичною поведінкою, а не за відчуттями.

## Сфера 1 — `MrrCalculator.calculate()`

- Business criticality: висока. Розраховує виручку, потрапляє до фінансових звітів.
- Change risk: високий. Один метод на 180 рядків, гілкування за типами плану та proration.
- Unknowns: точна поведінка proration під час зміни плану в середині місяця не покрита тестами.

## Сфера 2 — `PlanResolver.resolvePlan()`

- Business criticality: середня. Визначає активний план підписки на дату.
- Change risk: середній. Дубльована логіка lookup у двох місцях.
- Unknowns: поведінка при кількох overlapping-підписках не зафіксована.

## Сфера 3 — `MrrSummaryResponse` (public API)

- Business criticality: висока. Зовнішній контракт для дашборда та партнерських інтеграцій.
- Change risk: низький за кодом, але критичний за наслідками — зміна поля ламає споживачів.
- Unknowns: немає — контракт стабільний, поле `monthlyRecurringRevenue` публічне.

## Зведення пріоритетів

1. `resolvePlan()` — найкращий кандидат для першого Strangler-среза: середній ризик, локальний seam.
2. `calculate()` — потребує characterization tests до будь-якого refactor.
3. Public API чіпати не можна на поточній фазі.