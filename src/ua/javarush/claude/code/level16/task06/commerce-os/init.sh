#!/usr/bin/env bash
#
# Розгортає навчальний git-репозиторій для відпрацювання cherry-pick:
#   main                       — target branch (початковий стан, до перенесення)
#   feature/discount-label     — безпечна зміна мітки (лише promo-модуль)
#   feature/discount-contract  — та сама зміна мітки + непов'язана зміна API в checkout
# Ідемпотентний: повторний запуск нічого не ламає.
#
# Запуск (можна попросити Claude Code виконати за вас):
#   bash init.sh
#
set -euo pipefail

cd "$(dirname "$0")"

if [ -d .git ]; then
    echo "Репозиторій уже ініціалізовано — пропускаю."
    exit 0
fi

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаються
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"

# main = target branch (початковий стан: старий лейбл, стабільний контролер)
git add -A
git commit -q -m "base: target branch"
git branch -M main

# Трек 1: безпечна зміна мітки (лише promo-модуль)
git checkout -q -b feature/discount-label
git apply inputs/label-change.patch
git commit -q -am "discount-label: оновлений текст мітки"

# Трек 2: та сама зміна мітки + непов'язана зміна API в checkout (поза межами завдання)
git checkout -q main
git checkout -q -b feature/discount-contract
git apply inputs/label-change.patch
git apply inputs/contract-change.patch
git commit -q -am "discount-contract: мітка + зміна сигнатури контролера"

git checkout -q main

echo "Готово. Гілки:"
git branch
echo "Порада: безпечна зміна мітки лежить у feature/discount-label — її і потрібно cherry-pick'нути в main."