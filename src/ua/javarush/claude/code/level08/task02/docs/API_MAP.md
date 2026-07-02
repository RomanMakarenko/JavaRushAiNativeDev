# API Map — commerce-os-shop

> Згенеровано на основі аналізу вихідного коду `level08/task02`.
> Дата: 2026-07-02

---

## Записи

### 1. `POST /api/checkout` — оформлення та оплата кошика

| Поле | Значення |
|---|---|
| **Method/Path** | `POST /api/checkout` |
| **Handler** | [`CheckoutController.checkout()`](../src/main/java/com/example/shop/checkout/CheckoutController.java#L17-L19) — делегує `CheckoutService.process(request)`. |
| **Auth** | [`AuthFilter.isCustomerSessionValid()`](../src/main/java/com/example/shop/security/AuthFilter.java#L10-L12) — перевіряє, що `sessionToken != null && !sessionToken.isBlank()`. Як саме фільтр застосовується до маршруту — не визначено (див. **Open questions**). |
| **Integrations** | [StripeClient.charge()](../src/main/java/com/example/shop/payments/StripeClient.java#L17-L20) — потенційно викликається всередині `CheckoutService.process()`. |
| **Config** | [`application.yml`](../src/main/resources/application.yml#L7) — `payments.stripe.api-key: ${STRIPE_API_KEY}` (значення з оточення). <br> [`application.yml`](../src/main/resources/application.yml#L10) — `security.customer-session.cookie-name: cos_session`. |
| **Tests** | [`CheckoutControllerTest.checkout_returns400_forEmptyCart()`](../src/test/java/com/example/shop/checkout/CheckoutControllerTest.java#L17-L20) — перевіряє, що запит з `{}` повертає `400`. |
| **Open questions** | • `CheckoutService` не знайдено в коді — незрозуміло, як саме він викликає StripeClient і чи взагалі його викликає. <br> • `CheckoutRequest` / `CheckoutResponse` — модельні класи відсутні в репозиторії; невідома структура запиту й відповіді. <br> • `AuthFilter` не прив'язаний до маршруту — немає конфігурації `FilterRegistrationBean`, `SecurityFilterChain` або `@WebFilter`. <br> • Немає тесту на валідний сесійний токен (success path). <br> • Немає тесту на інтеграцію з Stripe (заглушка `ChargeResult.ok()`). |

---

### 2. StripeClient — зовнішня платіжна інтеграція

| Поле | Значення |
|---|---|
| **Method/Path** | N/A (внутрішній компонент, не API-ендпойнт) |
| **Handler** | [`StripeClient`](../src/main/java/com/example/shop/payments/StripeClient.java#L7-L20) — Spring `@Component` із методом `charge(ChargeRequest)`. |
| **Auth** | API-ключ, який передається через конструктор: `@Value("${payments.stripe.api-key}")`. |
| **Integrations** | Зовнішній сервіс Stripe (реальний HTTP-виклик на `api.stripe.com` закоментовано). |
| **Config** | [`application.yml`](../src/main/resources/application.yml#L7) — `payments.stripe.api-key: ${STRIPE_API_KEY}`. |
| **Tests** | Відсутні. `ChargeResult.ok()` — завжди успіх без реального HTTP-виклику. |
| **Open questions** | • `ChargeRequest` / `ChargeResult` — не знайдені в коді; невідомі поля запиту до Stripe. <br> • Реальна інтеграція з Stripe не реалізована — тіло методу `charge()` містить лише заглушку. <br> • Немає тестів на помилки Stripe (таймаут, недостатньо коштів, відхилений платіж). <br> • Немає тестів на конфігурацію — якщо `STRIPE_API_KEY` не задано, додаток упаде при старті. |

---

### 3. AuthFilter — фільтр автентифікації

| Поле | Значення |
|---|---|
| **Method/Path** | N/A (servlet filter, не API-ендпойнт) |
| **Handler** | [`AuthFilter.isCustomerSessionValid()`](../src/main/java/com/example/shop/security/AuthFilter.java#L10-L12) — перевіряє `sessionToken` на null та пустий рядок. |
| **Auth** | Власне і є механізмом авторизації (перевірка customer session). |
| **Integrations** | Немає. Покладається на наявність cookie `cos_session` (значення з конфігу). |
| **Config** | [`application.yml`](../src/main/resources/application.yml#L10) — `security.customer-session.cookie-name: cos_session`. |
| **Tests** | Відсутні. |
| **Open questions** | • `AuthFilter` не імплементує `jakarta.servlet.Filter` — це звичайний `@Component` без ланцюжка фільтрів. <br> • Немає реєстрації фільтра — незрозуміло, як він застосовується до запитів. <br> • Немає тестів на логіку валідації сесії (null, blank, валідний токен). <br> • Немає тестів на відсутність cookie (що станеться, якщо `cos_session` не передано). |

---

## Зведена інформація

### Конфігурація (`application.yml`)

| Ключ | Значення | Джерело |
|---|---|---|
| `spring.application.name` | `commerce-os-shop` | [application.yml:3](../src/main/resources/application.yml#L3) |
| `payments.stripe.api-key` | `${STRIPE_API_KEY}` (env) | [application.yml:7](../src/main/resources/application.yml#L7) |
| `security.customer-session.cookie-name` | `cos_session` | [application.yml:10](../src/main/resources/application.yml#L10) |

### Список API-ендпойнтів

| Method | Path | Controller | Статус |
|---|---|---|---|
| POST | `/api/checkout` | `CheckoutController` | Реалізовано (частково) |

### Зовнішні інтеграції

| Сервіс | Компонент | Статус |
|---|---|---|
| Stripe (платежі) | `StripeClient` | Заглушка — реальний HTTP-виклик не реалізовано |

### Покриття тестами

| Компонент | Тестів | Покрито | Не покрито |
|---|---|---|---|
| `CheckoutController` | 1 | Порожній кошик → 400 | Валідний кошик, невалідна сесія, помилка Stripe |
| `StripeClient` | 0 | — | Усе |
| `AuthFilter` | 0 | — | Усе |

### Відкриті питання (Open questions)

1. **`CheckoutService`** — де він визначений? Чи викликає він `StripeClient.charge()`? Без нього неможливо підтвердити інтеграцію checkout → Stripe за кодом.
2. **Модельні класи** — `CheckoutRequest`, `CheckoutResponse`, `ChargeRequest`, `ChargeResult` відсутні в репозиторії. Яка структура запиту до `/api/checkout`? Які поля повертаються?
3. **Ланцюжок фільтрів** — `AuthFilter` не імплементує `Filter` і не зареєстрований. Як саме він застосовується до маршруту `/api/checkout`?
4. **Stripe-заглушка** — `ChargeResult.ok()` завжди повертає успіх. Коли з'явиться реальна реалізація?
5. **Явна контрактна перевірка** — тест `checkout_returns400_forEmptyCart` не проходитиме, доки не з'явиться валідація `CheckoutRequest`. Чи планується додавання `@Valid` / Jakarta Bean Validation?