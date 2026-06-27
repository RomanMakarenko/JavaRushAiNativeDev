# TASK_SPEC — обов'язковий коментар для manual review

## Goal
Заборонити проведення повернення в режимі manual review без непорожнього коментаря оператора.

## Scope
- Лише сервісний шар `RefundService.java`.
- Додати unit-тест на сценарій із порожнім коментарем.

## Non-goals
- Інтеграція на рівні контролера.
- Обробка edge case `amount > 1000`.

## Acceptance criteria
- За `manualReview = true` і порожнього коментаря повернення не дозволено.
- Команда `./gradlew test --tests "*RefundService*"` успішно проходить.