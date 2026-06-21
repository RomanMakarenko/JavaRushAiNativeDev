#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій із НЕЗАКОМІЧЕНИМ bugfix (trim у label),
# щоб команда перегляду diff (`git diff` / `/diff`) показувала реальну зміну
# робочого дерева, за яким проводиться DoD-review.
#
# Сценарій після запуску:
#   - baseline-коміт: стан ДО фікса (normalizeLabel без trim);
#   - поверх у робочому дереві лежить НЕЗАКОМІЧЕНИЙ trim-fix
#     (inputs/trim-fix.patch) в OrderLabelService.java.
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

# робоче дерево зараз «після фікса» — відкочуємо trim-fix, фіксуємо baseline,
# потім накладаємо знову як незакомічену зміну.
git apply -R inputs/trim-fix.patch

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаємо
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"
git add -A
git commit -q -m "base: order label service before trim fix"
git branch -M main

git apply inputs/trim-fix.patch

echo "Готово. У робочому дереві лежить незакомічений trim-fix:"
echo "  git diff покаже реальну зміну для DoD-review."