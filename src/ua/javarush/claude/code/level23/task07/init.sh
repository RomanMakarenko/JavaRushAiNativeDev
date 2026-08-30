#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій Commerce OS з гілкою main та одним базовим
# комітом, щоб можна було створити sandbox-worktree.
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

git add -A
git commit -q -m "base: commerce-os"
git branch -M main

echo "Готово. Гілку main створено, можна створювати sandbox-worktree."