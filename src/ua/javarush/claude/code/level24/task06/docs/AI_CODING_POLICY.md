# AI Coding Policy

## Allowed

- AI може готувати код, тести, документацію та рефакторинг.
- AI може аналізувати repository, пропонувати зміни й готувати PR.
- Кожна AI-generated зміна проходить review так само, як human-written code.

## Review-required

- Будь-який AI-generated code має пройти review до merge.
- Зміни бізнес-логіки refund flow, ендпоінтів або конфігурації потребують review та quality gates.
- Зміни `db/migration/**` потребують explicit approval відповідального інженера.
- Перед PR визначте рівень ризику за [RISK_CLASSIFICATION.md](RISK_CLASSIFICATION.md).

## Disallowed without explicit approval

- Робота із `.env` або `secrets/` без explicit approval.
- Зміни схеми БД у `db/migration/**` без explicit approval відповідального інженера.
- Дії з деплоєм (`deploy`) без explicit approval.
- Операції з protected branch без explicit approval.
- Прямий push у protected branch `main`; зміни надходять через PR із review.

## Sensitive data

- Не додавайте секрети, токени або credentials у код, diff, логи чи PR.
- Не відкривайте й не змінюйте `.env` та `secrets/` без explicit approval.
- Перед відкриттям PR перевірте diff на секрети.

## PR expectations

- Перед відкриттям PR пройдіть blocking та перевірте advisory quality gates з [QUALITY_GATES.md](QUALITY_GATES.md).
- PR має містити одну логічну зміну, опис мети та визначений рівень ризику.
- Укажіть план відкоту, якщо PR зачіпає `db/migration/**`.
- Не використовуйте force push у protected branch.

## Enforcement references

- Рівні ризику та потрібні погодження: [RISK_CLASSIFICATION.md](RISK_CLASSIFICATION.md).
- Обов’язкові та advisory перевірки: [QUALITY_GATES.md](QUALITY_GATES.md).
- Шаблон PR містить checklist для quality gates, секретів, деплою, міграцій і protected branch.
- Hooks перевіряють відсутність секретів у diff на sensitive paths.

## Human ownership

- Відповідальний інженер надає explicit approval для high-risk змін.
- Людина, яка відкриває PR, відповідає за опис, risk level, результати quality gates і план відкоту.
- Human reviewer відповідає за рішення про merge; авторство AI не змінює критерії review.