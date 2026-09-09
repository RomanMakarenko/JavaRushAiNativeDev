#!/usr/bin/env bash
#
# Тест скрипта маскування: проганяємо sample-log через скрипт і порівнюємо
# результат з expected-log. Завершується з ненульовим кодом у разі розбіжності.

set -euo pipefail

DIR="$(cd "$(dirname "$0")/.." && pwd)"
SCRIPT="$DIR/scripts/redact-evidence.sh"
SAMPLE="$DIR/tests/fixtures/sample-log.txt"
EXPECTED="$DIR/tests/fixtures/expected-log.txt"

ACTUAL="$(bash "$SCRIPT" "$SAMPLE")"

if [[ "$ACTUAL" == "$(cat "$EXPECTED")" ]]; then
  echo "PASS: маскування збігається з expected-log.txt"
  exit 0
else
  echo "FAIL: вивід скрипта не збігся з expected-log.txt" >&2
  diff <(echo "$ACTUAL") "$EXPECTED" >&2 || true
  exit 1
fi