#!/usr/bin/env bash
# Перевіряє, що deterministic checks і AI review рознесені по різних job'ах
# і що AI review не є блокувальним.
set -euo pipefail

WF=".github/workflows/pr-checks.yml"

fail() {
  echo "FAIL: $1"
  exit 1
}

[ -f "$WF" ] || fail "Не знайдено файл $WF"

grep -Eq '^[[:space:]]+deterministic:' "$WF" || fail "Немає job deterministic"
grep -Eq '^[[:space:]]+ai-review:' "$WF" || fail "Немає job ai-review"

grep -q './gradlew test' "$WF" || fail "Немає команди ./gradlew test"
grep -q 'npm run lint' "$WF" || fail "Немає команди npm run lint"
grep -q 'npm run typecheck' "$WF" || fail "Немає команди npm run typecheck"
grep -q 'scripts/run-ai-review.sh' "$WF" || fail "AI review не використовує scripts/run-ai-review.sh"

grep -q 'continue-on-error: true' "$WF" || fail "AI review має бути advisory (continue-on-error: true)"

grep -q 'pull_request' "$WF" || fail "Workflow має запускатися на pull_request"

echo "OK: deterministic blocking і ai-review advisory розділені коректно"