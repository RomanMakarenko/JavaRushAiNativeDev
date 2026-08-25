# Нотатка про CI

Поточний workflow `pr-checks.yml` містить два jobs:

- `deterministic` — blocking. Запускає `./gradlew test`, `npm run lint`,
  `npm run typecheck` і `npm run secret:scan`.
- `ai-review` — advisory. Запускає `scripts/run-ai-review.sh` з
  `continue-on-error: true`, тому його падіння не зупиняє merge.

У разі падіння будь-якого deterministic step у логах має бути видно: яка перевірка
впала, її статус, посилання на лог як evidence і ймовірна причина.