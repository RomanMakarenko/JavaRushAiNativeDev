#!/usr/bin/env bash
# Перевіряє, що ci-triage.sh правильно класифікує збої за маркерами.
set -euo pipefail

HERE="$(cd "$(dirname "$0")" && pwd)"
TRIAGE="$HERE/ci-triage.sh"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

fail=0

check() {
  local marker="$1" expected="$2"
  local logfile="$TMP/case.log"
  printf '%s\n' "log line with $marker inside" > "$logfile"
  local got
  got="$(bash "$TRIAGE" "$logfile" | tr -d '[:space:]')"
  if [ "$got" != "$expected" ]; then
    echo "FAIL: marker '$marker' -> got '$got', expected '$expected'"
    fail=1
  else
    echo "OK: marker '$marker' -> $expected"
  fi
}

check "timeout"        "RERUN_ALLOWED"
check "network"        "RERUN_ALLOWED"
check "flaky"          "RERUN_ALLOWED"
check "regression"     "ROLLBACK_OR_FIX"
check "test failed"    "ROLLBACK_OR_FIX"
check "hook blocked"   "DISABLE_OR_REPAIR"
check "config invalid" "DISABLE_OR_REPAIR"

if [ "$fail" -ne 0 ]; then
  echo "VALIDATION FAILED"
  exit 1
fi

echo "VALIDATION OK"