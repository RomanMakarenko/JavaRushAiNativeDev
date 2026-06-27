# HANDOFF_NOTE — коментар для manual review

## Goal
Повернення в manual review потребує непорожнього коментаря оператора.

## Changed files
- `src/main/java/com/example/commerce/refund/RefundService.java`
- `src/test/java/com/example/commerce/refund/RefundServiceTest.java`

## Tests/checks
- `./gradlew test --tests "*RefundService*"` — зелений, 3 тести.

## Known risks
- Кейс `amount > 1000` поки що не покритий окремою перевіркою.
- Контролерний шар не чіпали; integration test ще не написано.

## Next step
Написати controller integration test для відмови в поверненні без коментаря.