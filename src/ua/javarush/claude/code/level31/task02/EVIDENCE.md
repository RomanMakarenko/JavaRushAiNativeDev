# EVIDENCE

Короткий журнал підтверджених змін по проєкту `landing-optimizer-mini`.
Кожен запис: що зроблено в межах slice і якою командою підтверджено результат.

## Записи

- Зафіксовано baseline release slice (ввід URL і кнопка `Analyze`). Перевірено: `npm test`.
- Кнопка `Analyze` вимикається для порожнього/пробільного URL і активується для непорожнього. Перевірено: `npm test -- --runTestsByPath tests/landing-form.test.tsx`.