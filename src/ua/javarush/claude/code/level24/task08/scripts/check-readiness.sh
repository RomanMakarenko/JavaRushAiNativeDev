#!/usr/bin/env bash
# Перевірка готовності shared asset до командного rollout.
# Поточна версія: перевіряє лише наявність README.
set -euo pipefail

ASSET_DIR="${1:?Usage: check-readiness.sh <asset-dir>}"

# Перевірка наявності README в каталозі asset.
if [ ! -f "$ASSET_DIR/README.md" ]; then
  echo "FAIL: у $ASSET_DIR немає README.md"
  exit 1
fi

echo "PASS: $ASSET_DIR готовий до rollout"