# RISK_MAP — CashFlow Dashboard

Карта ризиків побудована лише на фактичних описах архітектури та сирих доказах із `submissions/RISK_INPUTS.txt`. Кожен запис має посилання на конкретні рядки джерел.

## 1. Business-critical no-test zone — MRR pause/resume

- **Area:** `mrr-engine` — business-critical no-test zone; класифікація: `business-critical`.
- **Business criticality:** Висока. MRR є ключовим показником активних підписок, тому помилка в його розрахунку безпосередньо спотворює фінансову звітність.
- **Change risk:** Високий. Логіка pause/resume містить legacy-поведінку заморожування суми замість обнулення та залежить від серверного часу; зона не має автоматичних тестів і не має coverage report.
- **Risk categories:** `business-critical`, `untested behavior`, `time-dependent logic`, `recently changed`.
- **Evidence:** `docs/ARCHITECTURE_CURRENT.md:6-11` фіксує розрахунок MRR, legacy-quirk для `paused == true`, серверний час у `resume()` та відсутність автоматичних тестів; `submissions/RISK_INPUTS.txt:7-27` підтверджує відсутність тестів і coverage report, а також три коміти, включно з нещодавнім hotfix навколо pause.
- **Missing checks:** Немає автоматичних перевірок pause-resume, перевірки заморожування `lastBilledAmount`, поведінки на межах дат/часу та регресії після останнього hotfix.
- **Recommended action:** Додати до обов’язкових перевірок релізу сценарії pause-resume з фіксованим часом і очікуваним MRR для активної, paused та resumed підписки.

## 2. Configuration risk area — PaymentGateway secrets and provider key

- **Area:** `payments` — configuration risk area; класифікація: `configuration risk`.
- **Business criticality:** Висока. Неправильні або відсутні ключ провайдера чи webhook secret можуть зупинити списання або порушити перевірку платіжних webhook-ів.
- **Change risk:** Високий. Конфігурація розподілена між двома properties-файлами та env-подібними ключами, а `verifyWebhook()` і refund не мають тестового покриття.
- **Risk categories:** `configuration risk`, `secret management`, `untested integration path`, `payment integrity`.
- **Evidence:** `docs/ARCHITECTURE_CURRENT.md:14-16` вказує, що ключ провайдера і webhook secret читаються з properties через env-змінні, а покрито лише happy path `charge()`; `submissions/RISK_INPUTS.txt:33-42` називає `application.properties`, `payments.properties`, `PAYMENTS_PROVIDER_KEY` і `PAYMENTS_WEBHOOK_SECRET` та фіксує відсутність тестів для `verifyWebhook()` і refund.
- **Missing checks:** Немає перевірки повноти/узгодженості production-конфігурації, негативних випадків порожнього або неправильного secret, а також автоматичної перевірки webhook і refund flow.
- **Recommended action:** Додати startup/configuration check, який явно відхиляє неповні payment settings, і включити в обов’язкову перевірку сценарії invalid webhook secret та refund failure.

## 3. High-coupling area — payments callers

- **Area:** `payments` — high-coupling area; класифікація: `high-coupling`.
- **Business criticality:** Висока. Один gateway використовується у MRR, refund flow і billing, тому зміна його контракту може одночасно вплинути на кілька фінансових потоків.
- **Change risk:** Високий. Зона активно змінюється, але її перевірки охоплюють тільки happy path `charge()`, а залежні виклики мають спільну точку відмови.
- **Risk categories:** `high-coupling`, `cross-flow regression`, `payment integrity`, `active churn`.
- **Evidence:** `docs/ARCHITECTURE_CURRENT.md:18-20` прямо фіксує, що `payments` викликається з MRR, refund flow і billing та має високу coupling; `submissions/RISK_INPUTS.txt:33-49` підтверджує обмежене тестове покриття і три коміти в зоні, яку описано як активно змінювану.
- **Missing checks:** Немає перевірок сумісності gateway з MRR, refund і billing одночасно; немає тестів для `verifyWebhook()` та refund, які могли б виявити регресію спільного контракту.
- **Recommended action:** Зробити перевірку payment gateway обов’язковою для всіх трьох caller flows — MRR, refund і billing — перед прийняттям змін у `payments`.