#!/usr/bin/env bash
# Before-tool guard: блокує запис у файли .env* і друкує повідомлення.
# Приклад секрету, який не повинен потрапити в репозиторій: sk_live_EXAMPLE_NOT_REAL
FILE="${1:-<unknown>}"
echo "BLOCKED: запис у захищений шлях заборонено: ${FILE}" >&2
exit 1