# Inspect-only plan: `calculateMonthlyMrr`

## Goal

Запропонувати один маленький behavior-preserving refactor для
`MrrCalculator.calculateMonthlyMrr`: відокремити пошук тарифу від розрахунку
MRR, не змінюючи результат роботи методу.

## Planned transformation

Винести поточний цикл пошуку тарифу та обробку невідомого коду в один приватний
helper `resolvePlan(String planCode)`:

```java
private Plan resolvePlan(String planCode) {
    for (Plan plan : plans) {
        if (findByCode(plan, planCode)) {
            return plan;
        }
    }
    throw new IllegalArgumentException("Unknown plan code: " + planCode);
}
```

Після цього `calculateMonthlyMrr` використовуватиме helper і виконуватиме
формулу:

```java
Plan found = resolvePlan(planCode);
return found.getMonthlyPrice().multiply(BigDecimal.valueOf(seats));
```

Це один локальний refactor slice: лише структурне переміщення вже наявної
логіки пошуку в приватний метод. Порядок обходу `plans`, вибір першого збігу,
текст винятку та формула залишаються незмінними.

## Affected files

- `src/main/java/com/acme/cashflow/mrr/MrrCalculator.java` — єдиний файл
  застосунку, який може бути змінений під час майбутньої реалізації плану.
- `src/test/java/com/acme/cashflow/mrr/MrrCalculatorTest.java` — не змінювати;
  використовується як characterization baseline.
- `submissions/INSPECT_PLAN.md` — цей inspect-only план; змінюється зараз.

## Non-goals

- Не змінювати public API, зокрема сигнатуру
  `calculateMonthlyMrr(String planCode, int seats)`.
- Не змінювати business behavior: результат `monthlyPrice * seats`, порядок
  пошуку, поведінку для `seats` і точний текст
  `Unknown plan code: <code>` мають залишитися такими самими.
- Не змінювати `Plan`, `findByCode`, структуру даних `plans` або інші класи.
- Не додавати нові тести, не редагувати наявні тести й не розширювати scope
  refactor-а через `Optional`, stream API, нормалізацію кодів чи обробку `null`.

## Checks

Після окремого погодження реалізації виконати characterization test:

```bash
./gradlew test --tests "*MrrCalculatorTest"
```

Зокрема, мають залишитися зеленими:

- `MrrCalculatorTest.calculatesMrrForKnownPlan` — перевіряє формулу MRR;
- `MrrCalculatorTest.throwsLegacyMessageForUnknownPlan` — перевіряє legacy-текст
  винятку.

Джерело тестів: `src/test/java/com/acme/cashflow/mrr/MrrCalculatorTest.java`.
Також перевірити `git diff -- src/main/java src/test/java`, щоб підтвердити
відсутність змін у коді застосунку та тестах у цьому inspect-only проході.

## Rollback

Якщо characterization tests стануть червоними або diff покаже зміну
поведінки, відкотити майбутню реалізацію refactor-а в
`src/main/java/com/acme/cashflow/mrr/MrrCalculator.java` до попередньої версії,
повернувши inline-пошук у `calculateMonthlyMrr`. Файл тестів не відновлювати й
не редагувати, оскільки цей план не передбачає його змін.