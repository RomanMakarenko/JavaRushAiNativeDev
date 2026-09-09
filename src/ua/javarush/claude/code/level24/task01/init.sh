#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій Commerce OS з НЕЗАКОМІЧЕНИМИ змінами,
# щоб terminal-команди із задачі (`git status --short`, `git diff --name-only`)
# показували реальний робочий стан перед production decision gate.
#
# Сценарій після запуску:
#   - baseline-коміт: проект без правок refund-flow, міграція V103 уже в історії;
#   - поверх baseline у робочому дереві лежать НЕЗАКОМІЧЕНІ зміни:
#       * modified: README.md, RefundPolicyService.java, RefundAccessGuard.java
#         (накочуються з inputs/uncommitted-changes.patch);
#       * untracked: db/migration/V104__refund_policy.sql (нова міграція).
# Ідемпотентний: повторний запуск нічого не ламає.
#
# Запуск (можна попросити Claude Code виконати за вас):
#   bash init.sh
#
set -euo pipefail

cd "$(dirname "$0")"

if [ -d .git ]; then
    echo "Репозиторій уже ініціалізований — пропускаю."
    exit 0
fi

V104="db/migration/V104__refund_policy.sql"

# V104 — «нова» міграція: у baseline її ще немає, тому тимчасово прибираємо,
# щоб повернути як untracked-файл уже поверх baseline-коміту.
V104_BODY="$(cat "$V104")"
rm -f "$V104"

# baseline уже містить ранішу міграцію V103 — завдяки їй каталог
# db/migration/ tracked, і новий V104 у git status показується повним шляхом.
cat > db/migration/V103__orders_index.sql <<'SQL'
-- Міграція: індекс за замовленнями для прискорення вибірки
CREATE INDEX idx_orders_created_at ON orders (created_at);
SQL

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаються
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"

# робочі артефакти задачі не трекаємо, щоб вони не засмічували git status/diff,
# який студент збирає (інакше заповнений submissions/ сплив би сам в evidence)
cat > .gitignore <<'IGNORE'
init.sh
inputs/
submissions/
IGNORE

git add -A
git commit -q -m "base: commerce-os refund flow"
git branch -M main

# незакомічені правки tracked-файлів (README + два java)
git apply inputs/uncommitted-changes.patch

# нова міграція повертається як untracked-файл (git add її не робимо)
printf '%s\n' "$V104_BODY" > "$V104"

echo "Готово. У робочому дереві лежать незакомічені зміни:"
echo "  git status --short / git diff --name-only покажуть реальний стан."
