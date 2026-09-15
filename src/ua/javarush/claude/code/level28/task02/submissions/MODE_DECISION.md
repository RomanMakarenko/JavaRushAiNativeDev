# Mode Decision

## Chosen mode

**migration** — обрано рівно один режим. Підстава вибору — зміна середовища виконання та framework, а не загальна оцінка стану коду.

## Why not refactoring

Режим refactoring не обрано: рішення визначене зміною runtime/framework, а не внутрішньою перебудовою коду чи його структури.

## Why not modernization

Режим modernization не обрано: рішення визначене зміною runtime/framework, а не загальним оновленням функціональності, архітектури або інженерних практик.

## Non-goals

- Не виконувати cleanup `BillingService`.
- Не нормалізувати timestamps.

Документ не містить кроків реалізації, пропозицій щодо зміни коду або upgrade-команд.