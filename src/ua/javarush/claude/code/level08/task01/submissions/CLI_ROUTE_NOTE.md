# Route Note

## Public endpoint

**POST** `/api/orders` — створення замовлення з кошика клієнта  
**GET** `/api/orders/{id}` — отримання замовлення за ідентифікатором

Source: [`OrderController.java`](../src/main/java/com/example/shop/orders/OrderController.java)

---

## Internal endpoint

**POST** `/internal/jobs/retry-payments` — повторне надсилання завислих платежів (викликається планувальником)

Source: [`InternalJobsController.java`](../src/main/java/com/example/shop/jobs/InternalJobsController.java)

---

## Config dependency

- `payments.stripe.api-key` — береться зі змінною оточення `${STRIPE_API_KEY}`
- `queues.order-events-url` — береться зі змінною оточення `${QUEUE_URL}`
- `refunds.inbox-page-size` — розмір сторінки: `20`

Source: [`application.yml`](../src/main/resources/application.yml)

---

## Context snapshot

Після виконання `/context` і `/context all` у контексті CLI присутні наступні файли застосунку:

- `src/main/java/com/example/shop/orders/OrderController.java`
- `src/main/java/com/example/shop/jobs/InternalJobsController.java`
- `src/main/resources/application.yml`

Також у контексті: інформація про робочу директорію `level08/task01`, git-статус репозиторію, стандартні системні інструкції, MCP-інструменти та 14 підключених скілів.