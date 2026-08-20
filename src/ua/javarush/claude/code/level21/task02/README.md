# order-service

Сервіс обробки замовлень. Містить розрахунок суми повернення (refund) для скасованих замовлень.

## Структура

- `src/` — вихідний код сервісу.
- `inputs/` — вхідні артефакти для локальних bounded run (наприклад, diff-файли).
- `scripts/` — допоміжні скрипти delivery workflow.

## Запуск PR-summary

Генератор PR-summary — bounded run: вхід обмежений diff-файлом, вихід записується у вказаний markdown-файл, жодних комітів/push/merge.

```bash
scripts/generate-pr-summary.sh inputs/current.diff out/pr-summary.md
```

Скрипт викликає `claude -p` (print / неінтерактивний режим) з prompt-ом, що містить diff, і записує результат у файл з другого аргумента.