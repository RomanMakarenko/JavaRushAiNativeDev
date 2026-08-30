# Workflow Kit

Backend-сервіс Commerce OS. Містить refund flow (`payments/`), схеми БД
(`migrations/`) і конфігурацію середовища (`.env*`).

## L3 policy

Проєкт працює за L3 baseline:

- безпечне дослідження і тести дозволені автоматично (`allow`);
- будь-які записи потребують підтвердження (`ask`);
- protected paths і небезпечні git-команди заборонені (`deny`).

Деталі — у `.claude/settings.jsonc` і `.claude/CLAUDE.md`.