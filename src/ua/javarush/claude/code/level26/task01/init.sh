#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій legacy-сервісу cashflow-mrr для discovery:
# створює гілку legacy/mrr-discovery, історію з 5 комітів і незакомічену
# discovery-замітку в inputs/incident.md. Завдяки цьому команди git status,
# git log -5 і git branch із задачі показують реальний стан репозиторію.
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

# discovery відбувається в окремій гілці, а не в main
git checkout -q -b legacy/mrr-discovery

# перший коміт — поточний знімок проєкту
git add -A
git commit -q -m "feat: initial MrrSnapshotJob daily snapshot"

# змістовна історія розробки MRR-модуля (для розділу Recent commits)
git commit -q --allow-empty -m "refactor: move MRR normalization out of controller"
git commit -q --allow-empty -m "feat: include paused subscriptions in active set"
git commit -q --allow-empty -m "chore: add paused_at column migration (V2)"
git commit -q --allow-empty -m "fix: clamp negative proration in MrrCalculator"

# discovery-замітка «у роботі» — незакомічена правка, яку baseline фіксує
cat >> inputs/incident.md <<'NOTE'

## Discovery-замітка (у роботі)
- Кандидат на перевірку: нормалізація YEARLY → щомісячну в `MrrCalculator`.
NOTE

echo "Готово. Гілка legacy/mrr-discovery, історія з 5 комітів,"
echo "у inputs/incident.md лежить незакомічена discovery-замітка (git status)."