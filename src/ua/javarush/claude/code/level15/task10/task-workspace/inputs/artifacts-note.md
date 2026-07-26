# Нотатка щодо артефактів pipeline REFUND-204

Порядок інтеграції трьох ролей (merge order):
1. backend-owner — фікс у `src/api/orders` (перевірка ідемпотентності рефанда).
2. frontend-owner — блокування повторного кліку в `src/web/checkout`.
3. tests-owner — regression-тести поверх готового backend і frontend.

Що роль зобов’язана віддати на виході для наступного кроку:
- diff по своїх файлах;
- вивід прогону тестів своєї зони;
- один рядок-резюме, що саме змінилося.

Backend-owner інтегрується першим, тому його вихід — вхід для frontend-owner і
tests-owner.