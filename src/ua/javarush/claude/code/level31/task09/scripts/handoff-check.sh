#!/usr/bin/env bash
# Перевірка готовності handoff: усі наявні project checks мають пройти.
set -euo pipefail

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(cd -- "$SCRIPT_DIR/.." && pwd)

cd "$PROJECT_ROOT"
bash scripts/run-tests.sh
bash scripts/run-demo.sh
printf '%s\n' "HANDOFF_READY"
