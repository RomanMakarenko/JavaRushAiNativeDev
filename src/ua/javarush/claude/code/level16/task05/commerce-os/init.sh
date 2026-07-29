#!/usr/bin/env bash
#
# Розгортає навчальний git-репозиторій із двома feature-гілками поверх main.
# Ідемпотентний: повторний запуск нічого не ламає.
#
# Запуск (можна попросити Claude Code виконати за вас):
#   bash init.sh
#
set -euo pipefail

cd "$(dirname "$0")"

if [ -d .git ]; then
    echo "Репозиторій уже ініціалізовано — пропускаю (git branch покаже гілки)."
    exit 0
fi

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаються
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"

# Базовий стан = гілка main
git add -A
git commit -q -m "base: модулі checkout і promo"
git branch -M main

# Трек 1: рефакторинг обчислення підсумкової суми
git checkout -q -b feature/checkout-refactor
git apply --ignore-whitespace inputs/checkout-refactor.patch
git commit -q -am "checkout-refactor: округлення підсумкової суми"

# Трек 2: підвищена promo-знижка для коду SUMMER
git checkout -q main
git checkout -q -b feature/promo-discount
git apply --ignore-whitespace inputs/promo-discount.patch
git commit -q -am "promo-discount: знижка 15% для SUMMER"

git checkout -q main

echo "Готово. Гілки:"
git branch