# cashflow — compatibility matrix

Центральний артефакт рівня — evidence-backed compatibility matrix для першого стрибка
сервісу `cashflow` на Spring Boot 3.x і Java 21.

Входи вже зібрані:
- `docs/migrations/current-state.md` — поточний стек;
- `docs/migrations/BOOT3_RESEARCH.md` — research notes щодо Boot 3.x;
- `evidence/dependencies.txt` — фактичний dependency graph.

Завдання: на їх основі заповніть `docs/migrations/COMPATIBILITY_MATRIX.md`.
Матриця фіксує статуси за закритим словником і не переходить у phased plan або rollback.