# ARCHITECTURE_CURRENT — CashFlow Dashboard (фактична поведінка)

Документ описує, як сервіс працює сьогодні, без планів на майбутнє.

## Зона `mrr-engine`

- Клас `com.cashflow.mrr.MrrEngine` обчислює MRR за активними підписками.
- Legacy-quirk: при `paused == true` MRR не обнуляється, а заморожується
  на `lastBilledAmount`.
- `resume()` бере дату відновлення з годинника сервера (`System.currentTimeMillis()`).
- Автоматичних тестів на pause-resume немає.

## Зона `payments`

- Клас `com.cashflow.payments.PaymentGateway` проводить списання і перевіряє webhook.
- Ключ провайдера і webhook secret читаються з properties через env-змінні.
- Покрито тестом лише happy path `charge()`; `verifyWebhook()` і refund не покриті.

## Зв'язаність

- `payments` викликається з MRR, з refund flow і з білінгу — висока зв'язаність.