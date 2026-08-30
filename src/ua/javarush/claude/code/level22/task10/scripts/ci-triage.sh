#!/usr/bin/env bash
# CI triage classifier.
# На вході приймає шлях до лог-файлу і друкує рекомендовану реакцію.
#
# Класифікація збоїв за маркерами в лог-файлі:
#   timeout / network / flaky     -> RERUN_ALLOWED
#   regression / test failed      -> ROLLBACK_OR_FIX
#   hook blocked / config invalid -> DISABLE_OR_REPAIR
set -euo pipefail

LOG_FILE="${1:-}"

if [ -z "$LOG_FILE" ] || [ ! -f "$LOG_FILE" ]; then
  echo "usage: ci-triage.sh <log-file>" >&2
  exit 2
fi

# Найсерйозніша реакція має пріоритет: спершу конфіг/хуки,
# потім регресії, і лише в останню чергу тимчасові збої.
if grep -qiE 'hook blocked|config invalid' "$LOG_FILE"; then
  echo "DISABLE_OR_REPAIR"
elif grep -qiE 'regression|test failed' "$LOG_FILE"; then
  echo "ROLLBACK_OR_FIX"
elif grep -qiE 'timeout|network|flaky' "$LOG_FILE"; then
  echo "RERUN_ALLOWED"
else
  # Невідомий маркер — краще впасти голосно, ніж порадити невірну дію.
  echo "no recognized failure marker in $LOG_FILE" >&2
  exit 1
fi