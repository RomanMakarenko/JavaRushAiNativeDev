#!/usr/bin/env bash
# Перевірка готовності shared asset до командного rollout.
set -euo pipefail

ASSET_DIR="${1:?Usage: check-readiness.sh <asset-dir>}"
README="$ASSET_DIR/README.md"
ASSET_NAME="$(basename "$ASSET_DIR")"
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)"
CHANGELOG="$(cd -- "$SCRIPT_DIR/.." && pwd -P)/CHANGELOG.md"

# Перевірка наявності README в каталозі asset.
if [ ! -f "$README" ]; then
  echo "FAIL: у $ASSET_DIR немає README.md"
  exit 1
fi

# README має містити власника та інструкцію rollback.
if ! grep -Eq '^[[:space:]]*Owner:[[:space:]]*[^[:space:]]' "$README"; then
  echo "FAIL: у $README немає Owner:"
  exit 1
fi

if ! grep -Eq '^[[:space:]]*Rollback:[[:space:]]*[^[:space:]]' "$README"; then
  echo "FAIL: у $README немає Rollback:"
  exit 1
fi

# Asset має бути згаданий у changelog.
if [ ! -f "$CHANGELOG" ]; then
  echo "FAIL: немає CHANGELOG.md"
  exit 1
fi

if ! grep -Fq -- "$ASSET_NAME" "$CHANGELOG"; then
  echo "FAIL: у CHANGELOG.md немає запису про $ASSET_NAME"
  exit 1
fi

echo "PASS: $ASSET_DIR готовий до rollout"