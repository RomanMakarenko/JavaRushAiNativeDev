#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій проєкту, щоб команда `git grep` працювала
# по відстежуваних файлах (secret-scan).
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
# identity задається локально в репозиторії, глобальні налаштування не чіпаємо
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"

git add -A
git commit -q -m "base: config snapshot"
git branch -M main

echo "Готово. Репозиторій ініціалізовано, git grep працює по відстежуваних файлах."