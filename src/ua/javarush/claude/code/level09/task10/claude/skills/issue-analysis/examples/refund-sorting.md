# TASK SPEC

## Goal
Запити на повернення в inbox йдуть спочатку за високим пріоритетом, усередині
пріоритету — від старих до нових.

## Scope
Серверне сортування в `RefundRequestSorter.java`, виклик із
`RefundQueueService.java`.

## Non-goals
Не змінюємо фронтенд inbox, схему БД і контракт REST API.

## Acceptance criteria
- сортування за спаданням priority;
- всередині priority — за зростанням createdAt;
- тести модуля підтримки зелені.

## Verification
Unit-тест на RefundRequestSorter; gradle test модуля підтримки.

## Open questions
- Що вважати «високим пріоритетом» — який діапазон значень priority?
- Чи потрібен стабільний порядок за однакових createdAt?