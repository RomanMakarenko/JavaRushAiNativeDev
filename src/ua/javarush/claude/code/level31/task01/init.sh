#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій landing-optimizer-mini та відтворює
# робочий стан «small diff у роботі»: базовий коміт на гілці
# feature/disable-empty-analyze + незакомічені правки з inputs/pending-fix.patch.
# Завдяки цьому команди `git status`, `git diff --stat`, `git branch` із завдання
# показують реальний pending diff, який студент лише фіксує в baseline.
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

# робота йде у feature-гілці, а не в main
git checkout -q -b feature/disable-empty-analyze

# базовий коміт: demo slice без виправлення «порожнього URL»
git add -A
git commit -q -m "base: landing-optimizer-mini demo slice"

# очікувані правки (виправлення disabled-кнопки + тест) застосовуються в робоче дерево,
# але НЕ комітяться — це і є той самий small diff, baseline якого знімається
git apply inputs/pending-fix.patch

echo "Готово. Репозиторій на гілці feature/disable-empty-analyze,"
echo "у робочому дереві лежить незакомічений small diff (git status / git diff --stat)."