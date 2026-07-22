# Структура hook-конфігурації

Кожен hook у `Claude Workflow Kit for Team` читається як контракт із трьох частин:
`event` (коли), `matcher` (за якої умови), `handler` (що зробити).

## Поля

- `event` — момент життєвого циклу Claude Code, на який реагує hook.
  - `afterFileEdit` — після зміни файлу (after-edit / file changes).
  - `beforeToolUse` — перед використанням інструмента (before tool use).
  - `sessionStart` — старт сесії (session lifecycle).
- `matcher` — фільтр спрацьовування. Може містити:
  - `paths` — масив glob-шаблонів шляхів, наприклад `frontend/src/**/*.tsx`.
  - `tool` — ім'я інструмента, наприклад `Write`.
  - Для `sessionStart` matcher зазвичай не потрібен.
- `handler` — дія. Найчастіше:
  - `command` — команда або шлях до script. Плейсхолдер `{{file}}` підставляє
    шлях до зміненого файлу.

## Принципи

- Matcher тримаємо вузьким: краще `frontend/src/**/*.{ts,tsx}`, ніж «на всі файли».
- Handler має бути передбачуваним і корисним лише в межах вузького сценарію.
- Конфіг має залишатися валідним JSON (без коментарів усередині JSON).