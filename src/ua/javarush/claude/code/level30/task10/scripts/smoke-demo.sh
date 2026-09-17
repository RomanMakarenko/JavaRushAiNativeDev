#!/usr/bin/env bash
# Smoke-check demo-сценарію Refund Assistant.
# УВАГА: звертається до застарілого endpoint /api/refund/check.
set -euo pipefail

URL="http://localhost:8080/api/refund/check"
echo "smoke: перевіряю demo-endpoint $URL"
code=$(curl -s -o /dev/null -w "%{http_code}" "$URL" || echo 000)
if [ "$code" = "200" ]; then
  echo "smoke-demo: OK"
  exit 0
else
  echo "smoke-demo: FAILED (http $code)"
  exit 7
fi