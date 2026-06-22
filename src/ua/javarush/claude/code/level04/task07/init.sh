#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій ShopFlow з НЕЗАКОМІЧЕНИМ bugfix WEB-208,
# щоб команда показу diff (`git diff` / `/diff`) показувала реальну зміну
# робочого дерева, яку потрібно відрецензувати.
#
# Сценарій після запуску:
#   - baseline-коміт: стан ДО фіксу (frequency захардкожено як "weekly");
#   - поверх у робочому дереві лежить НЕЗАКОМІЧЕНИЙ bugfix
#     (inputs/bugfix-web-208.patch): NewsletterForm.tsx + тест.
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

# робоче дерево зараз у стані «після фіксу» — відкочуємо bugfix,
# щоб зафіксувати baseline-коміт (стан ДО), потім накладаємо знову.
git apply -R inputs/bugfix-web-208.patch

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаємо
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"
git add -A
git commit -q -m "base: newsletter form before WEB-208 fix"
git branch -M main

# bugfix повертається як НЕЗАКОМІЧЕНА зміна робочого дерева
git apply inputs/bugfix-web-208.patch

echo "Готово. У робочому дереві лежить незакомічений bugfix WEB-208:"
echo "  git diff покаже реальну зміну для review."