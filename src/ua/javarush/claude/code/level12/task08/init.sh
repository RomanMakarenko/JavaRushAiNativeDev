#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій auth-сервісу з НЕЗАКОМІЧЕНОЮ зміною,
# щоб команда показу diff (`/diff` / `git diff`) показувала реальне додавання
# методу refresh, який делегується review-субагенту reviewer.
#
# Сценарій після запуску:
#   - baseline-коміт: AuthController без методу refresh;
#   - поверх у робочому дереві лежить НЕЗАКОМІЧЕНЕ додавання методу refresh
#     (inputs/refresh-change.patch).
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

# робоче дерево зараз «після зміни» — відкатуємо додавання refresh,
# фіксуємо baseline, потім накочуємо знову як незакомічену зміну.
git apply -R inputs/refresh-change.patch

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаються
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"
git add -A
git commit -q -m "base: auth controller before refresh method"
git branch -M main

git apply inputs/refresh-change.patch

echo "Готово. У робочому дереві лежить незакомічене додавання методу refresh:"
echo "  /diff (git diff) покаже реальну зміну для review."