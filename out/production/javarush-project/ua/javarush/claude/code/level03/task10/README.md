# refunds-service — класифікація правил

Вхідний список правил — у `inputs/rules.md`.
Результат класифікації складається в `submissions/RULE_CLASSIFICATION.md`.

Місця дії правил:
- `Project-wide` — загальні правила проєкту (живуть у `CLAUDE.md`).
- `Task-specific` — межі конкретної задачі (живуть у `TASK_SPEC.md`).
- `Enforcement` — те, що забезпечується автоматикою (hooks, CI, branch protection), а не текстом постановки.