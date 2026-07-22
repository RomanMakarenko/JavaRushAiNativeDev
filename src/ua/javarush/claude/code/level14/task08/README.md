# Workflow Kit для команди — Commerce OS

Мета-репозиторій про сам Claude Code: правила, skills, agents, hooks і політики
команди Commerce OS. Тут немає product code — лише автоматизація workflow.

Шар автоматизації живе в `.claude/hooks/`. Деталі щодо розгортання hooks дивіться
в `hooks/README.md`. Список ступенів розгортання: `log-only`, `non-blocking`,
`formatting`, `test/report`, `narrow blocking`, `policy-managed`.