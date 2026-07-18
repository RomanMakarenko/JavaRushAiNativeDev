# MCP Diagnostics Report

**Generated:** 2026-07-18 — на основі `/mcp status`, `/mcp debug`, `claude mcp list`, `claude doctor` та ручної інспекції всіх шарів конфігурації.

---

## Visible servers

| Джерело | Серверів | Статус |
|---------|----------|--------|
| `claude mcp list` (еквівалент `/mcp status`) | **0** | `"No MCP servers configured. Use \`claude mcp add\` to add a server."` |
| `claude mcp debug` (еквівалент `/mcp debug`) | **0** | `"No MCP servers configured."` |
| `~/.claude/settings.json` — ключ `mcp` | **0** | Ключ відсутній |
| Проєктний `.claude/settings.json` | **0** | Файл не існує |
| Проєктний `.claude/settings.local.json` — ключ `mcp` | **0** | Ключ відсутній |

**Висновок:** Жоден MCP-сервер не додано через `claude mcp add` і не налаштовано в жодному settings-файлі.

---

## Connection status

### Плагіни (не активовані, не підключені)

15 MCP-маніфестів знайдено в `~/.claude/plugins/marketplaces/claude-plugins-official/external_plugins/`, але `enabledPlugins: {}` — жоден не ввімкнено:

| Server | Transport | Статус підключення |
|--------|-----------|--------------------|
| asana | SSE | ⏸ Не ввімкнено |
| context7 | stdio | ⏸ Не ввімкнено |
| discord | stdio | ⏸ Не ввімкнено |
| fakechat | stdio | ⏸ Не ввімкнено |
| firebase | stdio | ⏸ Не ввімкнено |
| github | HTTP | ⏸ Не ввімкнено |
| gitlab | HTTP | ⏸ Не ввімкнено |
| greptile | HTTP | ⏸ Не ввімкнено |
| imessage | stdio | ⏸ Не ввімкнено |
| laravel-boost | stdio | ⏸ Не ввімкнено |
| linear | HTTP | ⏸ Не ввімкнено |
| playwright | stdio | ⏸ Не ввімкнено |
| serena | stdio | ⏸ Не ввімкнено |
| telegram | stdio | ⏸ Не ввімкнено |
| terraform | stdio | ⏸ Не ввімкнено |

**Підсумковий статус:** 15 definitions, 0 enabled, **0 connected**.

### Marketplace-реєстри

| Marketplace | Джерело | Статус |
|-------------|---------|--------|
| `karpathy-skills` | GitHub `forrestchang/andrej-karpathy-skills` | ✓ Ресолвлено (це skills, не MCP) |
| `team-tools` | GitHub `RomanMakarenko/team-tools` | ✓ Ресолвлено (це skills, не MCP) |

---

## Project-local server definitions (в каталозі `mcp/`)

У поточному проєкті визначено конфігураційні stubs для двох серверів (файли не змінено):

| Server | Файл | Тип | Інструменти |
|--------|------|-----|-------------|
| **issue-tracker** | `mcp/issue-tracker.json` | HTTP, OAuth, `${ISSUE_TRACKER_URL}` | `list_issues` (ro), `get_issue` (ro), `search_issues` (ro) |
| **docs-lookup** | `mcp/docs-lookup.json` | stdio, `python scripts/docs_server.py` | `search_docs` (ro) |

Ці визначення **не завантажені** в Claude Code runtime — вони є проєктними артефактами (stubs для майбутнього налаштування).

---

## Observed issues

| № | Issue | Деталі |
|---|-------|--------|
| 1 | **Жодного підключеного MCP-сервера** | Усі діагностичні команди повертають чистий `"No MCP servers configured."` |
| 2 | **15 плагінів не активовано** | `enabledPlugins` порожній — жоден з 15 наявних .mcp.json manifests не завантажується |
| 3 | **Remote Control недоступний** | Середовище використовує кастомний `ANTHROPIC_BASE_URL` (`https://llm.javarush.com`), а не `api.anthropic.com` — Remote Control і claude.ai sign‑in не працюють |
| 4 | **Помилки відсутні** | Клієнт у чистому стані; жодних помилок підключення, таймаутів чи збоїв не зафіксовано |
| 5 | **Проєктні stubs не підключені** | `mcp/issue-tracker.json` та `mcp/docs-lookup.json` існують на диску, але не зареєстровані в Claude Code runtime |

### Конкретний статус з management surface

```
$ claude mcp list
No MCP servers configured. Use `claude mcp add` to add a server.

$ claude mcp debug
No MCP servers configured.
```

Жодної помилки — це свідома відсутність конфігурації в тренувальному/sandbox середовищі.

---

## Environment summary

| Параметр | Значення |
|----------|----------|
| Claude Code version | 2.1.214 (native, darwin-arm64) |
| Auth endpoint | `https://llm.javarush.com` |
| Model override | `claude-course-fast` / `claude-course-pro` / `claude-course-max` |
| Remote Control | ❌ |
| claude.ai sign-in | ❌ |
| Файли в `mcp/` | Не змінено |