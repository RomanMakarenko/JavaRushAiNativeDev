# Characterization-тести модуля MRR

Ці тести фіксують ПОТОЧНУ поведінку legacy-коду (навіть якщо вона дивна).
Вони потрібні як safety baseline перед будь-яким refactor: поки вони зелені,
поведінка не змінилася.

## Існуючі тести

### `MrrCalculatorTest.calculatesMrrForKnownPlan`
Фіксує, що для відомого тарифу MRR = `monthlyPrice * seats`.
Файл: `src/test/java/com/acme/cashflow/mrr/MrrCalculatorTest.java`

### `MrrCalculatorTest.throwsLegacyMessageForUnknownPlan`
Фіксує точний текст legacy-виключення `Unknown plan code: <code>`
за відсутності тарифу. Текст змінювати не можна.
Файл: `src/test/java/com/acme/cashflow/mrr/MrrCalculatorTest.java`

## Як запускати

```bash
./gradlew test --tests "*MrrCalculatorTest"
```