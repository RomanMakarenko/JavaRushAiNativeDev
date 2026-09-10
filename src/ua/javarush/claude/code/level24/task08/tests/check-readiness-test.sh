#!/usr/bin/env bash
# Тест для check-readiness.sh.
# Поточна версія перевіряє лише успішний сценарій із README.
set -euo pipefail

SCRIPT="scripts/check-readiness.sh"

# Випадок 1: коректний asset має проходити перевірку.
if bash "$SCRIPT" .claude/skills/issue-analysis >/dev/null; then
  echo "ok: коректний asset проходить перевірку"
else
  echo "FAIL: коректний asset не пройшов перевірку"
  exit 1
fi

echo "ALL TESTS PASSED"