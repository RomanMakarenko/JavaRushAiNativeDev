# QUICK DISABLE RESULT — `format-on-edit`

## До / Після

| Стан | `.claude/hooks/` | `.claude/hooks-disabled/` |
|------|------------------|---------------------------|
| **До** | `format-on-edit.json` | `.gitkeep` |
| **Після** | _порожньо_ | `format-on-edit.json`, `.gitkeep` |

## Вимкнений hook

- **Ім'я:** `format-on-edit`
- **Подія:** `afterFileEdit`
- **Matcher:** `frontend/src/**/*`
- **Handler:** `scripts/format-on-edit.sh {{file}}`

Hook переміщено до `.claude/hooks-disabled/`. Щоб увімкнути повторно, поверніть файл у `.claude/hooks/`.