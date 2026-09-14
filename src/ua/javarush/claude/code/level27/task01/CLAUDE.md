# Контекст: modernization safety baseline для mrr-engine

Робочий простір для legacy-модуля `mrr-engine` (CashFlow Dashboard, пакет
`com.acme.cashflow.mrr`). Обчислює Monthly Recurring Revenue за підписками.

## Наскрізний сценарій лекції

Перед будь-яким structural-рефакторингом потрібно зафіксувати поточну поведінку
(safety baseline). Завдання лекції працюють з одним і тим самим модулем:
знімаємо кандидатів через термінал, пишемо characterization test на legacy-quirk
з refund, оформлюємо підсумковий baseline-документ.

## Правила

- Baseline фіксує CURRENT behavior, а не бажану. Known bug не «виправляється»
  всередині characterization test — він фіксується як є.
- Код застосунку (`src/main`) у цих завданнях не змінюється.
- Нові залежності не додаються.

## Каталоги

- `src/main` — код модуля (read-only у межах baseline).
- `src/test` — тести та фікстури.