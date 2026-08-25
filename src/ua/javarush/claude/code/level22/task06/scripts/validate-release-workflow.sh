#!/usr/bin/env bash
# validate-release-workflow.sh — перевірка межі artifact vs action у release.yml.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
WF="$ROOT/.github/workflows/release.yml"

fail() { echo "FAIL: $1"; exit 1; }

[ -f "$WF" ] || fail "release.yml не знайдено"

# Має бути окремий job для draft release notes
grep -Eq '^[[:space:]]*draft[-_a-z]*:' "$WF" || fail "немає окремого job для draft release notes"

# Має бути безпечний ручний тригер для publish
grep -q "workflow_dispatch" "$WF" || fail "немає workflow_dispatch для ручного publish"

# У workflow не повинно бути production deploy step
if grep -Eqi 'deploy.*prod|production.*deploy' "$WF"; then
  fail "у workflow виявлено production deploy step"
fi

# Publish/tag job не повинен висіти на автоматичному push:
# перевіряємо, що publish-job явно прив’язаний до workflow_dispatch через if/needs-умову
grep -Eq 'if:.*workflow_dispatch' "$WF" || fail "publish job не обмежено workflow_dispatch"

echo "OK: draft і publish розділено, publish потребує ручного запуску"