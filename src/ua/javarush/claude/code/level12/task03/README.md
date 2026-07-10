# Workflow Kit

Набір scoped-агентів для командного workflow.

## Агенти
- `reviewer` — локальний code review без зовнішніх джерел.
- `tester` — проєктування тестів і запуск перевірок.
- `migration-assistant` — дослідження щодо міграцій і read-only docs lookup.

## Принцип
Кожен агент отримує мінімально достатній набір skills, MCP і memory scope
(least-privilege). Зайві capabilities прибирають під час рев’ю конфігурації.