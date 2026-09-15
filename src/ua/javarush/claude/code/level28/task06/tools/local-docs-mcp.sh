#!/usr/bin/env bash
# Локальний stdio MCP-сервер для пошуку в офіційних migration docs.
# Працює лише на читання: повертає фрагменти документації за запитом,
# не виконує правок коду, build і version bump.
set -euo pipefail

DOCS_DIR="${DOCS_DIR:-official-docs}"

# Заглушка stdio-циклу: реальна реалізація обслуговує MCP-протокол
# і шукає по файлах у "$DOCS_DIR" у режимі read-only.
exec node tools/docs-mcp-server.js --docs "$DOCS_DIR" --read-only