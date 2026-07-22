# Workflow Kit — Commerce OS

Командний набір налаштувань Claude Code для проєкту Commerce OS: rules, skills,
agents, hooks і policy-файли зовнішніх інструментів.

## Каталоги

- `.claude/mcp/` — transport-конфіги MCP servers (issue tracker, monitoring, docs lookup).
- `.claude/tool-policies/` — preflight-політики використання зовнішніх інструментів.
- `docs/` — довідка щодо полів preflight і шаблони документів.
- `scripts/` — валідатори policy та preflight-документів.

## Позиція за замовчуванням

Зовнішні інструменти підключаються в режимі `read-only first`. Переведення інструмента
в `write-capable` або в project scope робиться лише після оформленого
preflight. У кожного інструмента повинен бути kill switch — короткий шлях
вимкнення.

## Поточний контекст

Команда розбирає баг `COM-142`: refund-запити в support inbox сортуються
неправильно. Для аналізу issue та коментарів використовується read-only доступ до
issue tracker.