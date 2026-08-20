#!/usr/bin/env bash
# Генератор PR-summary як bounded run.
#
# Використання:
#   scripts/generate-pr-summary.sh <вхідний diff> <вихідний markdown>
#
# Вхід обмежений одним diff-файлом (перший аргумент), вихід — одним
# markdown-файлом (другий аргумент). Claude запускається в print /
# неінтерактивному режимі (claude -p), prompt передається через stdin,
# результат записується у вказаний файл. Скрипт нічого не комітить,
# не пушить і не мерджить; у режимі dontAsk будь-який виклик інструментів
# авто-відхиляється, тому запуск залишається bounded.

set -euo pipefail

if [ "$#" -ne 2 ]; then
    echo "Помилка: потрібно рівно два аргументи." >&2
    echo "Використання: $0 <вхідний diff> <вихідний markdown>" >&2
    exit 1
fi

INPUT_DIFF="$1"
OUTPUT_MD="$2"

if [ ! -f "$INPUT_DIFF" ]; then
    echo "Помилка: вхідний файл не існує: $INPUT_DIFF" >&2
    exit 1
fi

mkdir -p "$(dirname "$OUTPUT_MD")"

# Формуємо prompt разом з вмістом diff і передаємо через stdin — так жодні
# спеціальні символи diff не інтерпретуються шеллом, а вхід для Claude
# визначений повністю.
{
    echo "Ось diff змін для Pull Request:"
    echo ""
    cat "$INPUT_DIFF"
    echo ""
    echo "Напиши коротке PR-summary українською у форматі Markdown."
    echo "Не використовуй жодних інструментів, не змінюй файли, не коміть,"
    echo "не пуш і не мердж — просто поверни готовий Markdown-текст."
} | claude -p --permission-mode dontAsk --output-format text > "$OUTPUT_MD"

echo "PR-summary записано у: $OUTPUT_MD"