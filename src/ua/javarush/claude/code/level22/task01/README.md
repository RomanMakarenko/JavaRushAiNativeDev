# Commerce OS — CI gate

Quality gate проєкту побудований із двох шарів:

- **Deterministic checks** — blocking. Backend tests, frontend lint і typecheck.
  Падіння будь-якого з них зупиняє merge в `main`.
- **AI review** — advisory. Запускається скриптом `scripts/run-ai-review.sh`
  і не має блокувати merge.

Перевірити коректність workflow можна командою:

```bash
bash scripts/validate-workflow.sh
```