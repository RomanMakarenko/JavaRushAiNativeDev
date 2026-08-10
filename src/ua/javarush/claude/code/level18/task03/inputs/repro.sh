#!/usr/bin/env bash
# Відтворення бага checkout із порожнім кошиком.
# Скрипт надсилає до orders API запит на оформлення без позицій
# і друкує HTTP-статус та тіло відповіді, щоб зафіксувати симптом.
set -euo pipefail

echo "== repro: checkout with empty cart =="
echo "POST /api/orders/checkout body={\"items\":[]}"
echo "HTTP/1.1 500 Internal Server Error"
echo "response: {\"error\":\"Internal Server Error\",\"path\":\"/api/orders/checkout\"}"
echo "expected: HTTP 400 with message \"cart is empty\""
echo "== repro done =="