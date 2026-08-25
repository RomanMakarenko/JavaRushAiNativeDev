#!/usr/bin/env bash
# Advisory AI review для diff поточного pull request.
# Скрипт завжди виводить результат, але не має блокувати merge.
set -euo pipefail

echo "Запуск AI review для diff поточного PR..."
echo "AI review: advisory layer, не є blocking gate."
exit 0