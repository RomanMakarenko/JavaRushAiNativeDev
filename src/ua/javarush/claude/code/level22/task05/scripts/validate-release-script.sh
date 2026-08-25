#!/usr/bin/env bash
# validate-release-script.sh — перевірка, що release.sh безпечний у default-сценарії.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SCRIPT="$ROOT/scripts/release.sh"

fail() { echo "FAIL: $1"; exit 1; }

[ -f "$SCRIPT" ] || fail "release.sh не знайдено"
[ -x "$SCRIPT" ] || fail "release.sh має бути виконуваним"

# У default path не повинно бути реальних publish-дій
if grep -Eq '^[[:space:]]*git[[:space:]]+push' "$SCRIPT"; then
  fail "release.sh виконує git push у default path"
fi
if grep -Eq '^[[:space:]]*gh[[:space:]]+release[[:space:]]+create' "$SCRIPT"; then
  fail "release.sh виконує gh release create у default path"
fi
if grep -Eq '^[[:space:]]*git[[:space:]]+tag' "$SCRIPT"; then
  fail "release.sh створює git tag у default path"
fi

# Має бути явна згадка про manual approval
grep -qi "approval" "$SCRIPT" || fail "немає згадки про human approval для tagging/publish"

# Має оновлюватися/генеруватися DRAFT_NOTES.md
grep -q "DRAFT_NOTES.md" "$SCRIPT" || fail "release.sh не працює з release/DRAFT_NOTES.md"

echo "OK: release.sh безпечний у dry-run за замовчуванням"