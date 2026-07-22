# Hooks команди Commerce OS (Workflow Kit)

У цьому каталозі описано шар автоматизації Workflow Kit. Кожен hook розгортається
сходинками: `log-only` -> `non-blocking` -> `formatting` -> `test/report` ->
`narrow blocking` -> `policy-managed`. Починати завжди з `log-only`.

## Поточний реєстр hooks

| Hook              | Stage          | Category     | Призначення                                  |
|-------------------|----------------|--------------|---------------------------------------------|
| `format-on-edit`  | log-only       | non-blocking | Кандидати на форматування frontend-файлів |
| `lint-report`     | log-only       | non-blocking | М'який lint-сигнал за зміненим файлом      |
| `block-secrets`   | narrow blocking| blocking     | Захист запису в секретні шляхи               |

Поле `stage` у кожному конфігу `.claude/hooks/*.json` — це поточна ступінь
розгортання. Поле `category` відрізняє blocking hook від non-blocking.