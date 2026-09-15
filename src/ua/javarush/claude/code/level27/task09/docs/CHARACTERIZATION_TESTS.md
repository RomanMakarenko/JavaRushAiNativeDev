# Characterization tests — safety baseline для MRR-модуля

> Тести фіксують поточну поведінку legacy-коду як є, включно з quirks.
> Вони потрібні як safety baseline перед будь-яким refactor.

## Покриті сценарії

- `MrrCalculatorCharacterizationTest#monthlyPlan_fullMonth` — базовий розрахунок за повний місяць.
- `MrrCalculatorCharacterizationTest#annualPlan_proratedToMonthly` — річний план, приведений до місяця.
- `MrrCalculatorCharacterizationTest#refundQuirk_keepsLegacyRounding` — фіксує legacy-округлення під час повернення коштів.
- `PlanResolverCharacterizationTest#activePlanOnDate` — вибір активного плану на дату.

## Поточний статус baseline

- Усі characterization tests зелені на поточному commit.
- Запуск: `./gradlew test --tests "*Characterization*"`.
- Власник baseline: команда Billing Platform.

## Відомі прогалини (unknowns)

- Немає тесту на overlapping subscriptions у `resolvePlan()`.
- Немає тесту на зміну плану в середині місяця у `calculate()`.

## Success criteria для baseline

- Зелений прогін `*Characterization*` обов’язковий як gate перед кожним refactor slice.