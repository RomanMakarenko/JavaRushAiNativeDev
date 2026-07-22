#!/usr/bin/env bash
# Блокує запис у файли, що містять секрети (config/secrets/** або **/.env*).
FILE="$1"
echo "[block-secrets] заблоковано запис у захищений шлях: $FILE"
exit 1