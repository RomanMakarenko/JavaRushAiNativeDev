# Evidence log — ORDER-482

## Task

Вихідний task spec: `inputs/task-spec.md` — **ORDER-482: Пошук замовлення за email повертає закриті замовлення**.

Мета: `OrderQueryService.findByCustomerEmail()` має повертати лише активні замовлення зі статусами `NEW`, `PAID`, `SHIPPED`; замовлення зі статусами `CLOSED` і `CANCELLED` не мають потрапляти у видачу. Acceptance criteria: пошук повертає лише активні замовлення, додано regression-тест, наявні тести зелені.

## Context sources

- `inputs/task-spec.md` — вихідна специфікація задачі та acceptance criteria.
- `inputs/context-sources.md` — перелік матеріалів контексту для ORDER-482:
  - `docs/specs/ORDER-482.md` — специфікація задачі;
  - `src/main/java/com/example/commerce/orders/OrderQueryService.java` — код фільтрації;
  - `src/test/java/com/example/commerce/orders/OrderQueryServiceTest.java` — regression-тест;
  - тикет ORDER-482 у трекері — вихідне обговорення бага.
- `inputs/diff-summary.txt` — зведення змінених файлів і кількості рядків.
- `inputs/test-output.txt` — результат перевірок і статус тестів.
- `inputs/decision-note.md` — запис про власника фінального рішення та межі фіксу.

## Changes

За `inputs/diff-summary.txt`:

- змінено `src/main/java/com/example/commerce/orders/OrderQueryService.java`: 4 додані рядки, 1 вилучений рядок;
- змінено `src/test/java/com/example/commerce/orders/OrderQueryServiceTest.java`: додано 18 рядків для regression-тесту;
- разом: 2 файли змінено, 20 рядків додано, 1 рядок вилучено.

За `inputs/decision-note.md`, фікс обмежений фільтрацією активних замовлень, публічна сигнатура методу не змінювалася.

## Checks

Джерело: `inputs/test-output.txt`.

- `OrderQueryServiceTest > returnsOnlyActiveOrders_forCustomerEmail()` — **PASSED**.
- `OrderQueryServiceTest > existingSearchStillWorks()` — **PASSED**.
- Підсумок: **2 тести пройшли, 0 не пройшли**.
- Gradle: **BUILD SUCCESSFUL in 6s**.
- Виконання: `4 actionable tasks: 2 executed, 2 up-to-date`.

## Decision

Фінальне рішення мерджити fix прийняв **тимлід Maria Petrova** після рев'ю diff і перевірки зелених тестів.

## Sensitive data note

Секрети і реальні customer data не публікуються. Цей evidence log містить лише мінімально необхідний опис задачі, джерел, змін, перевірок і рішення; повний transcript Claude session не включено.