#!/usr/bin/env bash
# Helper: виводить унікальні директорії верхнього рівня зі списку шляхів.
# Приймає шлях до файлу зі списком шляхів як аргумент (за замовчуванням — inputs/changed-files.txt).
set -euo pipefail

HERE="$(cd "$(dirname "$0")/.." && pwd)"
INPUT="${1:-$HERE/inputs/changed-files.txt}"

if [ ! -f "$INPUT" ]; then
  echo "Помилка: файл не знайдено: $INPUT" >&2
  exit 1
fi

cut -d/ -f1 "$INPUT" | grep -v '^$' | sort -u