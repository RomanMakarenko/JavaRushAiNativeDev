# Commerce OS — модуль повернення купонів (навчальний зріз)

Зріз сервісу Commerce OS навколо повернення купонів.

## Відкрите issue

Див. `inputs/issue.md`: `POST /api/coupons/return` іноді відповідає `500` замість
контрольованої помилки валідації. Потрібно провести read-only дослідження в plan mode
і зібрати `Investigation Note`, не змінюючи код.

## Структура

- `src/main/java/com/example/store/returns/` — контролер, сервіс, обробник помилок і DTO.
- `src/test/java/com/example/store/returns/` — тести модуля.
- `inputs/issue.md` — текст тікета.
- `submissions/` — сюди зберігається `INVESTIGATION_NOTE.md`.