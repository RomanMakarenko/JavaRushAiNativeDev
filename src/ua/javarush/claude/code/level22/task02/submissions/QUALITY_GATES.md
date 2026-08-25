# Quality Gates — PR → main (Commerce OS)

> Зведення обов'язкових та інформаційних перевірок, що передують злиттю PR у гілку `main`.
>
> Джерела: `.github/workflows/pr-checks.yml`, `inputs/pr-policy.md`, `inputs/ci-note.md`, `inputs/reviewer-note.md`.

## PR → main

Шлях PR до гілки `main`:

1. **Відкриття PR** на `main` тригерє workflow `pr-checks` (тригер `pull_request`, гілки `[main]`).
2. **Запуск CI.** Паралельно виконуються два jobs:
   - `deterministic` — **blocking**, зупиняє merge у разі падіння будь-якого step;
   - `ai-review` — **advisory** (`continue-on-error: true`), падіння не зупиняє merge.
3. **Human review.** Reviewer із команди checkout переглядає diff і ставить approval.
4. **Merge** дозволено лише після проходження всіх quality gates.

Правила з політики:

- Зелений CI — **необхідна, але не достатня** умова: падіння будь-якого deterministic check блокує merge.
- **Human approval є обов'язковим** і не може бути замінений автоматикою.
- Результати `ai-review` та метрика `coverage-delta` мають рекомендаційний характер.

## Must-pass (deterministic)

Усі перевірки з job `deterministic` мають бути **зеленими**. Падіння будь-якої з них блокує merge.

| Check | Команда | Job | Роль |
| --- | --- | --- | --- |
| Backend tests | `./gradlew test` | `deterministic` | Blocking |
| Frontend lint | `npm run lint` | `deterministic` | Blocking |
| Frontend typecheck | `npm run typecheck` | `deterministic` | Blocking |
| Secret scan | `npm run secret:scan` | `deterministic` | Blocking |

## Must-pass (human)

- **Щонайменше один approval** від reviewer'а з команди checkout.
- Approval ставиться **тільки після ручного перегляду diff**; автоматика не може його замінити.
- Обов'язкові критерії ручного рев'ю (checkout-service):
  - зміна бізнес-логіки покрита тестами;
  - немає витоку secret'ів і хардкоду ключів;
  - міграції БД **зворотні**.
- Зелений CI не скасовує human review — це окремий обов'язковий шар.

## Informational

Перевірки, що збираються для спостереження і **не блокують** merge:

| Елемент | Джерело | Статус |
| --- | --- | --- |
| AI-assisted review (`bash scripts/run-ai-review.sh`) | job `ai-review`, `continue-on-error: true` | Advisory |
| `coverage-delta` | політика PR → main | Observability |

## Failure message contract

У разі падіння будь-якого **deterministic** step у логах обов'язково має бути видно:

1. **Яка перевірка впала** — назва step/check (напр. `Backend tests`).
2. **Статус** — `failed` / `error`.
3. **Evidence** — посилання на лог, за яким можна відтворити падіння.
4. **Ймовірна причина** — короткий опис, чому перевірка могла впасти.

Це дозволяє за одним повідомленням у логах ідентифікувати падіння, підтвердити його логом і визначити напрямок фіксу — без додаткового ручного копання в CI.