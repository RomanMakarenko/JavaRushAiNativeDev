# Roadmap outline — legacy MRR-модуль

## Current state

- `com.acme.cashflow.mrr` має високу фінансову критичність: результат потрапляє до фінансових звітів.
- Characterization baseline зелений: `./gradlew test --tests "*Characterization*"`; legacy rounding для refund зафіксовано.
- `MrrCalculator.calculate()` — великий метод із гілкуванням за типом плану та proration.
- `PlanResolver.resolvePlan()` має середній change risk і дубльовану lookup-логіку у двох місцях.
- Unknowns: overlapping subscriptions у `resolvePlan()` та зміна плану в середині місяця у `calculate()`.
- Public API `MrrSummaryResponse.monthlyRecurringRevenue` стабільний і на цій фазі не змінюється.

## Target outcomes

- Винести lookup активного плану за один локальний seam без зміни legacy-поведінки.
- Мати контрольоване перемикання між legacy та новим lookup-шляхом через один feature flag.
- Зберегти сумісність `MrrSummaryResponse` і зафіксовані quirks до окремого узгодження.
- Створити перевірений baseline для наступних малих refactor slices.

## Phase 1

**Strangler-срез:** ввести seam `PlanLookup` навколо `PlanResolver.resolvePlan()` і спрямувати через нього lookup активного плану. Це єдиний seam цього етапу.

- Новий шлях увімкнений лише feature flag `cashflow.mrr.v2-single-plan`; flag-off залишає legacy lookup.
- Не змінювати правила вибору плану, розрахунок MRR або public API.
- Перед зміною перевірити поточний characterization suite; після зміни повторити його та перевірити еквівалентність legacy/new lookup на покритих сценаріях.
- Тримати slice локальним: одна поведінкова межа й один commit.

## Rollback

- Основний варіант: вимкнути `cashflow.mrr.v2-single-plan` (flag-off), щоб негайно повернути legacy lookup.
- Якщо потрібне повне повернення зміни: виконати revert одного commit, що містить цей Strangler-срез.
- Після rollback повторно запустити `./gradlew test --tests "*Characterization*"` і зафіксувати результат.

## Success criteria

- `./gradlew test --tests "*Characterization*"` завершується успішно у 100% прогонів перед merge та після rollback-перевірки.
- Для 100% покритих characterization-сценаріїв результати legacy і нового `PlanLookup` збігаються.
- У Phase 1 змінюється рівно один lookup seam; `MrrSummaryResponse.monthlyRecurringRevenue` не змінюється.
- Перемикання flag-off повертає legacy шлях без зміни коду та без повторного deploy.
- Rollback через revert одного commit відновлює зелений characterization suite.

## Non-goals

- Framework upgrade — поза scope цієї roadmap.
- Schema changes — поза scope цієї roadmap.
- Зміна public API або поля `monthlyRecurringRevenue`.
- Переписування `MrrCalculator.calculate()`, зміна proration чи виправлення legacy rounding.
- Розв’язання невідомих сценаріїв overlapping subscriptions і mid-month plan change в межах цього одного slice.