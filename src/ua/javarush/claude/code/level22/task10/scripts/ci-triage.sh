#!/usr/bin/env bash
# CI triage classifier.
# На вході приймає шлях до лог-файлу і друкує рекомендовану реакцію.
#
# ПОТОЧНА ПРОБЛЕМА: script рекомендує RERUN_ALLOWED для будь-якого збою.
# Через це тимчасові збої та реальні регресії лікуються однаково.
set -euo pipefail

LOG_FILE="${1:-}"

if [ -z "$LOG_FILE" ] || [ ! -f "$LOG_FILE" ]; then
  echo "usage: ci-triage.sh <log-file>" >&2
  exit 2
fi

# Поки що одна й та сама реакція на все — це і є баг, який треба виправити.
echo "RERUN_ALLOWED"