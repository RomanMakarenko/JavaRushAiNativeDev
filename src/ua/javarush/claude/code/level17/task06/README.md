# Commerce OS — модуль погашення купонів (навчальний зріз)

Mini-repo сервісу Commerce OS навколо погашення купонів.

## Відкритий issue

Див. `inputs/issue.md`: `POST /api/coupons/redeem` іноді відповідає `500` замість
controlled validation error. Потрібно зробити read-only investigation у plan mode,
оформити `docs/INVESTIGATION_NOTE.md` і підтвердити його локальним validator-ом.

## Структура

- `src/main/java/com/example/store/coupons/` — controller, service, обробник помилок і DTO.
- `src/test/java/com/example/store/coupons/` — тести модуля.
- `inputs/issue.md` — текст тікета.
- `tools/validate_investigation.py` — validator обов'язкових секцій note.
- `docs/` — сюди складається `INVESTIGATION_NOTE.md`.
- `submissions/` — сюди складається `VALIDATION.txt`.