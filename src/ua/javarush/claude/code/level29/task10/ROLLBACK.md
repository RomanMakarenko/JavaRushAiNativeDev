# ROLLBACK — план відкату міграції часу до UTC

## Коли відкатуватися
- Розбіжності у звітах після перемикання читання на `normalized_ts_utc`.
- Помилки dual-write на кроці backfill.

## Кроки відкату
1. Вимкнути прапорець `reports.migration.use-utc-column` (читання повертається на legacy `reported_at`).
2. Вимкнути прапорець `reports.migration.dual-write-utc`.
3. За потреби відновити таблицю з backup, знятого перед expand-кроком.
4. Повідомити Release manager про факт відкату.

## Межа незворотності
Відкат можливий до фази contract включно. Після видалення legacy-поля відкат
виконується лише з backup.