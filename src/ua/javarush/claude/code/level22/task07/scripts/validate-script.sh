#!/usr/bin/env bash
# Валідатор helper-скрипта changed-areas.sh.
# Перевіряє: скрипт приймає аргумент, не падає на еталонному вході,
# виводить лише унікальні директорії верхнього рівня.
set -euo pipefail

HERE="$(cd "$(dirname "$0")/.." && pwd)"
INPUT="$HERE/inputs/changed-files.txt"
SCRIPT="$HERE/scripts/changed-areas.sh"

if [ ! -f "$SCRIPT" ]; then
  echo "FAIL: scripts/changed-areas.sh не знайдено"
  exit 1
fi

OUT="$(bash "$SCRIPT" "$INPUT")" || { echo "FAIL: скрипт завершився з помилкою"; exit 1; }

EXPECTED=$'backend\ndocs\nfrontend'
ACTUAL="$(printf '%s\n' "$OUT" | sort -u | sed '/^$/d')"

if [ "$ACTUAL" != "$EXPECTED" ]; then
  echo "FAIL: очікувалися унікальні верхні директорії backend, docs, frontend"
  echo "Отримано:"
  echo "$ACTUAL"
  exit 1
fi

echo "OK: changed-areas.sh виводить коректні директорії верхнього рівня"