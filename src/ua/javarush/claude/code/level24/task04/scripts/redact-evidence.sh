#!/usr/bin/env bash
#
# redact-evidence.sh — маскує чутливі дані в журналі перед
# публікацією в EVIDENCE_LOG.md.
#
# ЗАРАЗ маскується ЛИШЕ order ID. E-mail і bearer token пропускаються,
# що небезпечно для публікуваного evidence. Потрібно доопрацювати.
#
# Використання: ./scripts/redact-evidence.sh <log-file>

set -euo pipefail

LOG="${1:?потрібен шлях до log-файлу}"

# Маскуємо order ID виду ORDER-123 -> ORDER-[REDACTED]
sed -E \
  -e 's/ORDER-[0-9]+/ORDER-[REDACTED]/g' \
  -e 's/[[:alnum:]._%+-]+@[[:alnum:].-]+\.[[:alpha:]]{2,}/[REDACTED_EMAIL]/g' \
  -e 's/(Bearer )[[:alnum:]._~+\/-]+/\1[REDACTED_TOKEN]/g' \
  "$LOG"