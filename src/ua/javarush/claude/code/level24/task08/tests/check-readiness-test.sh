#!/usr/bin/env bash
# Тест для check-readiness.sh.
set -euo pipefail

TEST_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd -P)"
SCRIPT="$TEST_DIR/scripts/check-readiness.sh"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

mkdir -p "$TMP/scripts" "$TMP/issue-analysis"
cp "$SCRIPT" "$TMP/scripts/check-readiness.sh"

run_check() {
  bash "$TMP/scripts/check-readiness.sh" "$TMP/issue-analysis" >/dev/null
}

write_fixture() {
  printf '%s\n' "$1" > "$TMP/issue-analysis/README.md"
  printf '%s\n' "$2" > "$TMP/CHANGELOG.md"
}

# Коректний asset має проходити перевірку.
write_fixture $'Owner: team\nRollback: remove the asset' 'Updated issue-analysis asset.'
if run_check; then
  echo "ok: коректний asset проходить перевірку"
else
  echo "FAIL: коректний asset не пройшов перевірку"
  exit 1
fi

# Без Owner: перевірка має завершуватися помилкою.
write_fixture 'Rollback: remove the asset' 'Updated issue-analysis asset.'
if run_check; then
  echo "FAIL: asset без Owner: пройшов перевірку"
  exit 1
fi

echo "ok: asset без Owner: відхилено"

# Без Rollback: перевірка має завершуватися помилкою.
write_fixture 'Owner: team' 'Updated issue-analysis asset.'
if run_check; then
  echo "FAIL: asset без Rollback: пройшов перевірку"
  exit 1
fi

echo "ok: asset без Rollback: відхилено"

# Без запису про asset у CHANGELOG.md перевірка має завершуватися помилкою.
write_fixture $'Owner: team\nRollback: remove the asset' 'Updated another asset.'
if run_check; then
  echo "FAIL: asset без запису в CHANGELOG.md пройшов перевірку"
  exit 1
fi

echo "ok: asset без запису в CHANGELOG.md відхилено"

echo "ALL TESTS PASSED"