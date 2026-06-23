# Evidence log: помилка оплати на checkout

> Зібрано на чистому контексті після `/clear`. Тільки відібрані докази, без шуму.

## Goal
Зафіксувати мінімальний набір доказів причини, через яку замовлення `A-20517`
залишається у статусі `PAYMENT_FAILED`, хоча шлюз повернув `succeeded`.

## Selected evidence
1. `inputs/payment-callback.log`
   - Надходить callback від `acme-pay` з `event=payment.succeeded` та `status=succeeded`.
   - Далі система логує `status mapping returned UNKNOWN for provider status=succeeded`.
   - Фінальний запис: `order status set to PAYMENT_FAILED orderId=A-20517`.
   - Прямий зв'язок: `succeeded` у шлюзі → `PAYMENT_FAILED` у нашій системі.

2. `src/main/java/com/example/payments/PaymentStatusService.java`
   - Метод `mapStatus` використовує `switch` із кейсами лише для `failed` та `pending`.
   - Статус `succeeded` відсутній у гілках → потрапляє в `default`, який повертає `PAYMENT_FAILED`.
   - Рядок 13 містить коментар: `// BUG: "succeeded" тут не обробляється і потрапляє в UNKNOWN -> PAYMENT_FAILED`.

3. `inputs/issue.md`
   - Підтверджує симптом: гроші списані (`succeeded` у шлюзі), але статус замовлення не оновлюється.
   - Користувач бачить «Оплата не пройшла, спробуйте ще раз».

## Excluded noise
- `inputs/old-payment-runbook.md` — описує провайдера `legacy-pay`, якого вже немає в системі.
- `inputs/checkout-error.log` — лише підтверджує UI-симптом (показ помилки на екрані), корінної причини не розкриває.
- `inputs/screenshot-description.md` — опис UI (червоний банер, кнопка повтору), дублює Issue, нової інформації не додає.

## Sensitive data
- `secrets/leaked-token.txt` — містить рядок `ACME_PAY_WEBHOOK_SECRET=whsec_...`
  (токен платіжного шлюзу). Для аналізу причини бага не потрібен.
  Увага: файл знаходиться в репозиторії — потенційний витік секретів.

## Open questions
1. Який офіційний мапінг статусів для `acme-pay`? Чи має `succeeded` мапитися в `PAID`?
2. Чи є інші статуси `acme-pay` (наприклад, `pending`, `failed`, `refunded`), які теж падають у `default`?
3. Чи потрібна джоба reconciliation для виправлення вже завислих замовлень зі статусом `PAYMENT_FAILED`?
4. Детальніше — у `EVIDENCE_GAPS.md`.