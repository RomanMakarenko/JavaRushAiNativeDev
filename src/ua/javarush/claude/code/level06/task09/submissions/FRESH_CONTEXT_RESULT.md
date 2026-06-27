# FRESH_CONTEXT_RESULT

## Goal
Заборонити проведення повернення в режимі manual review без непорожнього коментаря оператора.

## Current state
- Реалізовано в сервісному шарі (`RefundService.java`) — при `manualReview = true` та порожньому коментарі повернення не дозволяється.
- Додано unit-тест у `RefundServiceTest.java` на сценарій із порожнім коментарем.
- `./gradlew test --tests "*RefundService*"` — зелений, 3 тести.

**Відомі ризики:**
- Кейс `amount > 1000` поки що не покритий окремою перевіркою.
- Контролерний шар не чіпали; integration test ще не написано.

**Змінені файли:**
- `src/main/java/com/example/commerce/refund/RefundService.java`
- `src/test/java/com/example/commerce/refund/RefundServiceTest.java`

## Next step
Написати controller integration test для відмови в поверненні без коментаря.