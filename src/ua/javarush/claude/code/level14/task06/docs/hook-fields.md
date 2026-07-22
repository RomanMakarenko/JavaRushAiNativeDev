# Структура hook-конфігурації

Кожен hook у `Claude Workflow Kit for Team` читається як контракт із трьох частин:
`event` (коли), `matcher` (за якої умови), `handler` (що зробити).

## Поля

- `event` — момент lifecycle Claude Code, на який реагує hook.
  - `afterFileEdit` — після зміни файлу (after-edit / file changes).
  - `beforeToolUse` — перед використанням інструмента (before tool use).
  - `sessionStart` — старт сесії (session lifecycle).
- `matcher` — фільтр спрацьовування. Може містити:
  - `paths` — масив glob-шаблонів шляхів, наприклад `**/.env*`.
  - `tool` — назва інструмента, наприклад `Write`.
- `handler` — дія. Найчастіше:
  - `command` — команда або шлях до script. Плейсхолдер `{{file}}` підставляє
    шлях до цільового файлу.

## Принципи guard-hook

- Before-tool guard повинен мати вузький matcher: лише потрібний інструмент і
  лише небезпечні шляхи.
- Guard-handler друкує коротке повідомлення і завершується ненульовим exit code,
  щоб заблокувати дію.
- Конфіг має залишатися валідним JSON (без коментарів усередині JSON).