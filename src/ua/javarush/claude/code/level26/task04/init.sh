#!/usr/bin/env bash
#
# Ініціалізує git-репозиторій CashFlow Dashboard з історією комітів,
# щоб git churn (аналіз частоти змін) був відтворюваним: MrrEngine.java
# об'єктивно є файлом проєкту, що змінюється найчастіше. Дат не проставляє — гаряча
# точка визначається кількістю комітів на файл, а не календарем.
# Ідемпотентний: повторний запуск нічого не ламає. Після прогону робоче дерево
# збігається з release-станом проєкту.
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

ENG="legacy/com/cashflow/mrr/MrrEngine.java"
# фінальний (release) стан MrrEngine зберігаємо, щоб повернути останнім комітом
ENG_FINAL="$(cat "$ENG")"

git init -q
git config user.email "student@javarush.local"
git config user.name "JavaRush Student"
git branch -M main

# --- c1: первинний знімок усього проєкту; MrrEngine у простому вигляді ---
cat > "$ENG" <<'JAVA'
package com.cashflow.mrr;

import java.math.BigDecimal;
import java.util.List;

public class MrrEngine {
    public BigDecimal computeMrr(List<BigDecimal> activeSubscriptions) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal sub : activeSubscriptions) {
            total = total.add(sub);
        }
        return total;
    }
}
JAVA
git add -A
git commit -q -m "feat: initial CashFlow Dashboard skeleton"

# --- c2: MrrEngine — додано churnAdjustment ---
cat > "$ENG" <<'JAVA'
package com.cashflow.mrr;

import java.math.BigDecimal;
import java.util.List;

public class MrrEngine {
    public BigDecimal computeMrr(List<BigDecimal> activeSubscriptions, BigDecimal churnAdjustment) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal sub : activeSubscriptions) {
            total = total.add(sub);
        }
        return total.subtract(churnAdjustment);
    }
}
JAVA
git add -A
git commit -q -m "feat: subtract churnAdjustment in computeMrr"

# --- c3: MrrEngine — додано annualize ---
cat > "$ENG" <<'JAVA'
package com.cashflow.mrr;

import java.math.BigDecimal;
import java.util.List;

public class MrrEngine {
    public BigDecimal computeMrr(List<BigDecimal> activeSubscriptions, BigDecimal churnAdjustment) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal sub : activeSubscriptions) {
            total = total.add(sub);
        }
        return total.subtract(churnAdjustment);
    }

    public BigDecimal annualize(BigDecimal mrr) {
        return mrr.multiply(new BigDecimal("12"));
    }
}
JAVA
git add -A
git commit -q -m "feat: add annualize to MrrEngine"

# --- c4: MrrEngine — Javadoc про вік/нестабільність ---
ENG_C4="$(cat "$ENG")"
printf '%s' "$ENG_C4" | sed 's#public class MrrEngine {#/**\n * Рушій розрахунку MRR (monthly recurring revenue).\n * Найстаріша і найбільш часто виправлювана частина сервісу. Тестів майже немає.\n */\npublic class MrrEngine {#' > "$ENG"
git add -A
git commit -q -m "docs: note MrrEngine as oldest hot path"

# --- c5: MrrEngine — фінал (release-стан з FIXME/TODO щодо debt) ---
printf '%s\n' "$ENG_FINAL" > "$ENG"
git add -A
git commit -q -m "docs: flag proration/churn debt in computeMrr"

echo "Готово. Репозиторій ініціалізовано, історію розгорнуто."
echo "git churn (git log --pretty=format: --name-only | sort | uniq -c | sort -rn)"
echo "покаже MrrEngine.java як файл, що змінюється найчастіше."