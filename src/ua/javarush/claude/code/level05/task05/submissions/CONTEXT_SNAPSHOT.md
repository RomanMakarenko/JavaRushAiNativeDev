# Context Snapshot — 2026-06-25

_Створено після команди `/context` (альтернативна назва: `/context all`)._

## Conversation state

Повідомлень у діалозі на момент знімка: 5.
Контекст завантажено на 639 / 200k токенів — 0.3% від ліміту.

## Files in current work

- `TASK_SPEC.md` — опис задачі: виправити порожній екран після неправильного пароля під час входу в Commerce OS.
- `src/auth/LoginController.java` — контролер логіну; баг у методі `submit()`: при невдалій аутентифікації повертається `""` замість `"auth/login"`.
- `tests/AuthFlowTest.java` — тести auth-сценаріїв; тест `wrongPasswordShowsErrorMessage` падає через баг у контролері.

## Project instructions

Використовується файл `CLAUDE.md`, який містить правила роботи над проєктом Commerce OS (auth-модуль на Java 25 + Spring Boot 4): змінювати лише те, що стосується поточного завдання з TASK_SPEC.md; не чіпати public API сесійного шару; не додавати нові зовнішні залежності.

## Memory files

- `CLAUDE.md` — проєктні інструкції (275 токенів).

## MCP tools

- `mcp__ide__getDiagnostics` — сервер `ide`.

## Skills

- 13 built-in skills (разом ~1.5k токенів): `deep-research`, `update-config`, `keybindings-help`, `verify`, `code-review`, `simplify`, `fewer-permission-prompts`, `loop`, `claude-api`, `run`, `init`, `review`, `security-review`.

## Статус задачі (level05/task05)

- **Баг:** `LoginController.submit()` повертає `""` при невдалій аутентифікації замість `"auth/login"`.
- **Рішення (без зміни коду):** змінити `return ""` на `return "auth/login"` у `LoginController.java`; оновити `renderLoginError()` у `AuthFlowTest.java`.
- **Наразі змінено:** нічого не змінено — виконано лише читання файлів.