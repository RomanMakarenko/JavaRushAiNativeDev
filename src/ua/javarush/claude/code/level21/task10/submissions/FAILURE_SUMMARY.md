# Зведення про падіння CI — level21/task10

Джерело: [inputs/ci-failure.log](../inputs/ci-failure.log)

## Failure type

`code`

## Likely root cause

Дефект у продакшн-коді інбоксу повернень (refund inbox): запит/сортування повертає відшкодування в порядку зростання (старіші перші), тоді як вимога — новіші перед старішими. Інтеграційний тест `RefundInboxIntegrationTest.newerRefundsAppearBeforeOlder` очікує порядок `[REF-3001, REF-3000, REF-2999]` (за зменшенням), а отримує точно інвертований `[REF-2999, REF-3000, REF-3001]`. Порядок збігає системно, тож це не флейкі-падіння, а логічна вада сортування в коді (наприклад, відсутній/неправильний `ORDER BY created_at DESC` або сортування за замовчуванням у висхідному напрямку).

## Evidence

- Рядок 10 — тест падає: `RefundInboxIntegrationTest > newerRefundsAppearBeforeOlder() FAILED`
- Рядок 12 — детерміноване неспівпадіння порядку: `Expected refund order [REF-3001, REF-3000, REF-2999] but was [REF-2999, REF-3000, REF-3001]`
- Рядок 15 — місце падіння: `at com.example.commerce.refunds.RefundInboxIntegrationTest.newerRefundsAppearBeforeOlder(RefundInboxIntegrationTest.java:41)`
- Рядки 3–7 — середовище, збірка й юніт-тести коректні (`BUILD SUCCESSFUL in 33s`, unit tests exit code 0): проблема локалізована в логіці сортування, а не в інфраструктурі, залежностях чи правах доступу.

## Rerun scope

Перезапуск окремого тесту:

```bash
./gradlew :integrationTest --tests "com.example.commerce.refunds.RefundInboxIntegrationTest.newerRefundsAppearBeforeOlder"
```

Падіння детерміноване, тому сам по собі перезапуск без змін у коді знову закінчиться невдачею. Спочатку виправити сортування інбоксу повернень (новіші відшкодування першими), після чого повторити запуск зазначеного тесту, а потім — повний таск `:integrationTest` (або весь пайплайн), щоб підтвердити відсутність регресій.