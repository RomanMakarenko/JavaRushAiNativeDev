#!/usr/bin/env bash
# Layer 2 gate: перевіряє, що до change додано непорожню risk-note.
# Risk-note є обов'язковою для будь-якої checkout-зміни перед Layer 3.
set -uo pipefail

NOTE="${1:-RISK_NOTE.md}"

if [ ! -s "$NOTE" ]; then
    echo "FAIL: risk-note $NOTE відсутня або порожня"
    exit 1
fi

echo "PASS: risk-note $NOTE присутня і не пуста"
exit 0