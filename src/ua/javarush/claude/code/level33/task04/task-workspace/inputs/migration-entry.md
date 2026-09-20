# Cashflow — migration plan

- Domain: data/billing migration.
- Проблема: розрахунок MRR спирався на legacy-джерело, яке заважало подальшому розвитку.
- Зона відповідальності розробника: фазовий план expand → backfill → switch → contract, feature flags, rollback.
- Роль AI: допомога у формулюванні плану та parity-перевірок під ревізією розробника.
- Verification: parity test між legacy і v2 lookup; кожна фаза за окремим флагом.
- Result: підготовлено безпечний поетапний план міграції без втрати даних.
- Публічне посилання: https://github.com/example/cashflow/blob/main/MIGRATION_PLAN.md
- Limitations: документ описує план, фактичне виконання поза цим артефактом.
- Внутрішній staging URL (НЕ публікувати): https://internal.cashflow.local/admin