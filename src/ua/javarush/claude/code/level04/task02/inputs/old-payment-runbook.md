# Старий runbook по платежах (застарілий)

> Документ описує інтеграцію з попереднім провайдером `legacy-pay` (вимкнено у 2024).
> Поточний провайдер — `acme-pay`, мапінг статусів інший. Залишено для історії.

- Статуси legacy-pay: `OK`, `FAIL`, `PENDING`.
- Мапінг: `OK -> PAID`, `FAIL -> PAYMENT_FAILED`.
- Callback надходив на старий endpoint `/legacy/callback`.