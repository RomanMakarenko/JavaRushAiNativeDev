# Facts-first аналіз у plan mode

Робочий простір для розбору bug report по endpoint `POST /api/login`
без зміни коду.

## Структура
- `inputs/bug-report.md` — вихідний bug report (read-only, не змінювати).
- `submissions/FACTS_FIRST.md` — facts-first документ: відокремлює observed behavior
  від гіпотез.
- `submissions/COMMAND_USED.txt` — команда запуску Claude Code у plan mode.

## Ідея
Claude Code запускається в режимі `--permission-mode plan`: він може читати й
аналізувати, але не виконує edits. Це безпечний спосіб зібрати чистий input
для наступного кроку — bugfix відкладається, спочатку фіксуються факти та гіпотези.