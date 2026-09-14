# RISK_MAP.md — CashFlow Dashboard

Карта зон підвищеного change risk legacy-сервісу підписок.

## Область: mrr-engine / pause-resume

- Business criticality: high (впливає на MRR-знімки та звіти)
- Test coverage: none
- Change risk: high
- Чому: підписка на паузі залишається в active set (`MrrFormulas.java:23`),
  що розходиться з `BILLING_RULES.md`. Тестів на pause/resume немає.

## Область: payments / retry

- Business criticality: high (впливає на churn і continuity revenue)
- Test coverage: partial (лише happy-path retry)
- Change risk: medium-high
- Чому: churn dip між failed payment та успішним retry не відстежується.

## Область: refunds / proration

- Business criticality: medium-high (грошова логіка)
- Test coverage: partial (один happy-path тест)
- Change risk: medium
- Чому: edge cases proration (refund у день білінгу, refund більше залишку)
  не покриті тестами.