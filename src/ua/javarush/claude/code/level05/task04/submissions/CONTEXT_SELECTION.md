# Context Selection — AUTH-318: сесія скидається після refresh-токена

## Include in initial context

| Файл | Причина |
|---|---|
| `issue.md` | Основний опис бага: AUTH-318, симптоми (5% випадків → екран входу), підозра на гонку в `SessionService`, зачеплені модулі. Точка входу в задачу. |
| `SessionService.java` | Метод `rotateAccessToken` — імовірне джерело багу: спочатку видаляє стару сесію, потім додає нову (вікно гонки). Ключовий файл для виправлення. |
| `auth-error.log` | Лог з WARN/ERROR, які одразу відповідають сценарію: `session not found for rotated token`, `concurrent refresh detected`. Підтверджує гіпотезу про race condition. |

## Keep available

| Файл | Причина |
|---|---|
| `AuthFlowTest.java` | Тест `sessionSurvivesTokenRefresh` — падає на поточному коді. Знадобиться для перевірки фіксу. |
| `LoginController.java` | Контролер, що викликає `rotateAccessToken`. Корисний для розуміння точки входу, але змін, імовірно, не потребує. |
| `application.log` | Лог нормальної роботи (запити refresh, ордери). Стане в пригоді, щоб відрізнити штатну поведінку від аварійної. |
| `application.log` | Лог нормальної роботи — записи з інших модулів (OrderService, Catalog, Mailer). Якщо вся проблема в auth-error.log, ці записи не релевантні. |

## Exclude

| Файл | Причина |
|---|---|
| `dashboard-notes.md` | Чернетка ідей для дашборда (retention, сегменти). До авторизації й токенів не має жодного стосунку. |

> **Примітка:** `dashboard-notes.md` міг би опинитися і в "Keep available", але згідно з описом (`До авторизації стосунку не має`) його включення лише засмічує контекст. Виключаємо.

## Sensitive

| Файл | Причина |
|---|---|
| `.env.example` | DB_URL, DB_PASSWORD, JWT_SIGNING_KEY, STRIPE_SECRET_KEY — хоч і `example / replace-me`, але шаблон конфігурації з реальними іменами ключів і псевдосекретами. У контекст не кладемо з принципу. |