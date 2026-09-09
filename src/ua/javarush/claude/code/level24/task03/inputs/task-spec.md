# ORDER-482 — Пошук замовлення за email повертає закриті замовлення

## Бажана поведінка
`OrderQueryService.findByCustomerEmail()` має повертати тільки активні
замовлення (`NEW`, `PAID`, `SHIPPED`). Закриті (`CLOSED`, `CANCELLED`) у видачу
не потрапляють.

## Acceptance criteria
- Пошук за email повертає тільки активні замовлення.
- Додано regression-тест, наявні тести зелені.
