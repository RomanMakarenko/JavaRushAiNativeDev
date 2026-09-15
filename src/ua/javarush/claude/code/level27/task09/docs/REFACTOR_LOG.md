# Refactor log — модуль MRR

> Робочий лог невеликих безпечних кроків. Кожен запис — один slice з перевірками.

## Запис 1

- Phase: Phase 0 — safety baseline.
- Owner: команда Billing Platform.
- Checks: `./gradlew test --tests "*Characterization*"` — зелені.
- Next boundary: ввести seam `PlanLookup` навколо `resolvePlan()` без зміни поведінки.

## Запис 2

- Phase: Phase 1 — перший Strangler-срез.
- Owner: команда Billing Platform.
- Checks: `./gradlew test --tests "*Characterization*"`, `git diff --stat` залишається малим.
- Next boundary: увімкнути новий шлях тільки за feature flag `cashflow.mrr.v2-single-plan`.

## Відкриті питання

- Success criteria для всієї дорожньої карти поки не зафіксовані централізовано.
- Rollback описано тільки на рівні окремого slice, загального phase-rollback немає.