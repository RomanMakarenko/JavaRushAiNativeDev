#!/usr/bin/env bash
# Layer 2 gate: детерміноване сканування на секрети в джерельному коді checkout-модуля.
# Завершується з non-zero, якщо в коді зустрічається secret-like значення.
set -uo pipefail

TARGET="${1:-src}"

if grep -RInE 'sk_live_[A-Za-z0-9_]+|API_KEY=' "$TARGET" 2>/dev/null; then
    echo "FAIL: у $TARGET виявлено secret-like значення"
    exit 1
fi

echo "PASS: secret-like значення не знайдено в $TARGET"
exit 0