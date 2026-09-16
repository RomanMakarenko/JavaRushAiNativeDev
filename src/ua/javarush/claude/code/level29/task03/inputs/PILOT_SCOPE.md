# Pilot scope: Boot 3 migration (read-only)

- Цільовий модуль: `reports`
- Режим pilot: read-only (лише читання звітів, без запису в БД)
- Мета: перевірити сумісність модуля reports зі Spring Boot 3 на обмеженому зрізі
- Out of scope: модулі billing, auth, будь-які write-операції
- Тип pilot: запускається на окремому worktree, deploy у production не виконується