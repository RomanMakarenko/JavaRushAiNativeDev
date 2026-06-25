# TASK_SPEC: refund-threshold-m1

## Goal
Винести manual approval threshold refund-flow із захардкоженого значення `100`
в `RefundPolicyService` у конфігурацію, не змінюючи публічний API refund endpoint.

## Scope
Лише refund-flow: `RefundPolicyService` і конфігурація застосунку.

## Non-goals
- Не змінювати `RefundController` і публічний API.
- Не чіпати інтеграцію з платіжним провайдером.
- Не змінювати схему БД.

## Milestones
- M1 — винести threshold у конфігурацію і прочитати його в сервісі.
- M2 — закріпити поведінку тестами.
- M3 — фінальна перевірка і підготовка до передачі.

## Stop points
Після кожного milestone: зведення + перевірки + невеликий commit.