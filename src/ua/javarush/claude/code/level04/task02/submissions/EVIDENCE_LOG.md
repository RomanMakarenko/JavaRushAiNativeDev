# Evidence log: помилка оплати на checkout

> Зібрано на чистому контексті після `/clear`. Тільки відібрані докази, без шуму.

## Goal
Зафіксувати мінімальний набір доказів причини, через яку замовлення `A-20517`
залишається у статусі `PAYMENT_FAILED`, хоча шлюз повернув `succeeded`.

## Selected evidence
- `inputs/payment-callback.log` — видно `status=succeeded`, потім `status mapping returned
  UNKNOWN` і `order status set to PAYMENT_FAILED` для одного й того ж `orderId=A-20517`.
- `src/main/java/com/example/payments/PaymentStatusService.java` — метод `mapStatus`
  не обробляє `succeeded` і відводить його в `default -> PAYMENT_FAILED`.
- `inputs/issue.md` — опис симптому: гроші списані, замовлення в `PAYMENT_FAILED`.

## Excluded noise
- `inputs/old-payment-runbook.md` — застарілий runbook по провайдеру `legacy-pay`,
  інший мапінг статусів, до поточного `acme-pay` не належить.
- `inputs/checkout-error.log` — підтверджує лише UI-симптом (показ помилки), причини не дає.

## Sensitive data
- `secrets/leaked-token.txt` — webhook secret платіжного шлюзу. Вміст не копіюється,
  під час роботи доступ до файлу не потрібен.

## Open questions
- Повний список статусів `acme-pay` і коректний мапінг для `succeeded`.
- Чи потрібна reconciliation-джоба для вже завислих замовлень у `PAYMENT_FAILED`.