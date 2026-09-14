# Контекст: modernization safety baseline для mrr-engine

Робочий простір по legacy-модулю `mrr-engine` (CashFlow Dashboard, пакет
`com.acme.cashflow.mrr`). Обчислює Monthly Recurring Revenue за підписками.

## Наскрізний сценарій лекції

Перед будь-яким structural-рефакторингом потрібно зафіксувати поточну поведінку
(safety baseline). Завдання лекції працюють з одним і тим самим модулем:
збираємо кандидатів через термінал, пишемо characterization test на legacy-quirk
із refund, оформлюємо підсумковий baseline-документ.

## Правила

- Baseline фіксує CURRENT behavior, а не бажане. Known bug залишається як є
  і не «виправляється» всередині baseline-документа.
- Усі факти беруться з `inputs/`. Нічого не вигадувати.
- Код застосунку і тестів у цьому завданні не змінюється.

## Каталоги

- `inputs/` — behavior inventory, risk map, downstream notes, existing tests (read-only).
- `docs/` — підсумковий baseline-артефакт.