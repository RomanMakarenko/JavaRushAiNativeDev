# ARCHITECTURE_CURRENT.md — CashFlow Dashboard

Знімок поточної структури сервісу підписок (як працює сьогодні, без прикрас).

## Підсистема: subscription

- `SubscriptionService` — pause/resume/refund верхнього рівня.
- `MrrFormulas` — розрахунок щоденного MRR-знімка і правило active set.
- Pause змінює статус на `PAUSED`, але з active MRR підписка не видаляється.

## Підсистема: payment

- `PaymentRetryService` — повтор оплати після failed payment.
- Успішний retry позначає attempt як resolved; churn-метрика між failed
  і retry не перераховується.

## Підсистема: refunds

- `RefundService.applyPartialRefund` зменшує суму підписки за proration.

## Відомі розбіжності

- `BILLING_RULES.md` стверджує, що paused-підписка поза active MRR; код робить інакше.