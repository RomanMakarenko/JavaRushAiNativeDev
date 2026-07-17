# Workflow Kit — minimal MCP capability draft

Цей каталог містить чернетку мінімального capability contract для зовнішнього
issue tracker команди Commerce OS.

## Що не так із поточною чернеткою

`mcp/issue-tracker-draft.json` зараз описує issue tracker як внутрішній API
застосунку (`internal-api`) з правом на запис і мережевими деталями. Це хибно
для базової mental model теми: issue tracker — це зовнішня система, до якої
Claude отримує controlled access через MCP, а не внутрішній модуль codebase.

## Що має вийти

Мінімальна чернетка зовнішньої capability рівно з чотирма полями:

- `kind` — тип capability (`external-capability`).
- `server` — ім'я зовнішнього MCP-сервера (`commerce-issue-tracker`).
- `tools` — список доступних read-only tools.
- `access` — режим доступу (`read-only`).

Деталі transport, endpoint, auth і authScopes на цьому рівні НЕ потрібні — їх
не має бути у файлі.

## Перевірка

```
python scripts/validate_capability.py mcp/issue-tracker-draft.json
```

Команда має надрукувати `PASS`.