#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій з історією reports-модуля, щоб команди
# `git status`, `git log`, `git worktree` із задачі працювали по живому репо.
# Розгортає 3 коміти, що зачіпають MonthlyRevenueController.java,
# щоб `git log --oneline -- <файл>` показав реальну історію.
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

CTRL="src/main/java/com/cashflow/reports/MonthlyRevenueController.java"
# кінцевий стан контролера зберігаємо, щоб повернути його останнім комітом
FINAL_CTRL="$(cat "$CTRL")"

git init -q
# identity задається локально в репозиторії, глобальні налаштування не чіпаємо
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"
git branch -M main

# --- Коміт 1: скелет контролера ---
cat > "$CTRL" <<'JAVA'
package com.cashflow.reports;

import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер місячної виручки. Read-only поверхня.
 */
@RestController
public class MonthlyRevenueController {
}
JAVA
git add -A
git commit -q -m "feat: initial reports module skeleton"

# --- Коміт 2: винесення залежності в сервіс ---
cat > "$CTRL" <<'JAVA'
package com.cashflow.reports;

import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер місячної виручки. Read-only поверхня.
 */
@RestController
public class MonthlyRevenueController {

    private final MonthlyRevenueService revenueService;

    public MonthlyRevenueController(MonthlyRevenueService revenueService) {
        this.revenueService = revenueService;
    }
}
JAVA
git add -A
git commit -q -m "refactor: extract MonthlyRevenueService from controller"

# --- Коміт 3: кінцевий стан (read-only endpoint) ---
printf '%s\n' "$FINAL_CTRL" > "$CTRL"
git add -A
git commit -q -m "feat: add monthly revenue read-only endpoint"

echo "Готово. Репозиторій ініціалізовано, історію reports-модуля розгорнуто."
echo "git log --oneline -- $CTRL покаже 3 коміти."