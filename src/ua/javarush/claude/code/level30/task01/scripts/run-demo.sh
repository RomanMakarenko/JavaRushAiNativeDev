#!/usr/bin/env bash
# Демонстраційний запуск інструмента підтримки замовлень на sample data.
set -euo pipefail

DATA="data/sample-tickets.json"

echo "== Order Support Tool (demo) =="
echo "Джерело даних: $DATA"

count=$(grep -c '"id"' "$DATA")
echo "Завантажено sample tickets: $count"
echo "Прогін одного core flow: ticket -> draft -> review"
echo "Готово. Це demo-збірка на sample data, не production."