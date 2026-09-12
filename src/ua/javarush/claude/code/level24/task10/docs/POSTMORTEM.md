# Postmortem: повторна хибна high-risk класифікація

## Symptom
Reviewer skill вдруге за два тижні класифікував невеликий refund-PR як `HIGH` і заблокував merge. PR #441 мав diff на 12 рядків, regression test і не змінював логіку статусів; merge затримано на пів дня.

## Root cause
Правило ризику класифікує зміни лише за шляхом: каталог `refunds` безумовно означає `HIGH`. Воно не враховує розмір diff, тип змін або наявність тестів, тому broad safety-поріг перетворює назву каталогу на достатню умову для блокування без аналізу фактичного впливу.

## Evidence
- `.claude/skills/reviewer/risk-rules.md` містить правило `refunds/` → `HIGH (завжди)`.
- Reviewer для PR #441 послався лише на каталог; аналогічний хибний finding був у PR #418.
- Двоє інженерів підтвердили, що finding хибний.

## Workflow asset to update
`.claude/skills/reviewer/risk-rules.md`: замінити безумовне правило на класифікацію з урахуванням diff та тестів і додати regression-приклади для малих безпечних refund-змін.

## Owner
Власник reviewer skill і його правил класифікації ризику.

## Follow-up check
Прогнати reviewer skill на PR #441 та щонайменше один аналогічний малий refund-PR із regression test: результат не повинен блокувати merge як `HIGH` лише через шлях; окремо перевірити, що зміни з реальною платіжною логікою й далі отримують `HIGH`.