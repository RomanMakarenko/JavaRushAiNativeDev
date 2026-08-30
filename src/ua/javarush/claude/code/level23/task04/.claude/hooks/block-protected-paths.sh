#!/usr/bin/env bash
# Заготовка хука для блокування protected paths.
# Увага: наразі перевіряє лише payments/** і НЕ підключена до hooks.json.
set -euo pipefail

TARGET_PATH="${1:-}"

# protected path: каталог payments
case "$TARGET_PATH" in
  payments/*|*/payments/*)
    echo "BLOCKED: $TARGET_PATH належить до payments/** (protected)" >&2
    exit 1
    ;;
esac

exit 0