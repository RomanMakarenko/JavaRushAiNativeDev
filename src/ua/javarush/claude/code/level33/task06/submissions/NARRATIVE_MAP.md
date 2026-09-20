# Career narrative map

## Target role

**Middle backend developer / internal tools** у продуктовій команді середнього розміру, що працює з наявним кодом.

**Обраний narrative track:** delivery-focused backend engineer для надійної роботи з наявним кодом.

Цей track підкреслює повний цикл інженерної задачі: від issue та плану до реалізації, відтворюваного тесту, review і документації. Він не позиціонує кандидата як системного архітектора чи спеціаліста з CI/CD.

## Strongest skills

1. Ведення backend-задачі повним циклом: issue → plan → implementation → tests → review → docs.
2. Діагностика незнайомого коду через characterization checks і локалізація причини дефекту.
3. Reproducible regression tests, які відтворюють баг до фіксу.
4. Small, контрольований diff і робота з review trail.

## Evidence per skill

| Skill | Evidence |
|---|---|
| Повний delivery cycle | `TASK_SPEC.md` і `PR-walkthrough.md` для RF-217: формулювання задачі, план, small diff, пояснення змін і review. `capstone-README.md` показує доведений до відтворюваного demo core flow. |
| Діагностика незнайомого коду | RF-217: самостійно розібрана причина неправильного порядку refund-запитів в inbox. `refactor-case.md`: characterization checks підтвердили збереження поведінки під час зміни структури розрахунку кошика. |
| Regression testing | `regression-tests.md`: reproducible тести відтворюють RF-217 до фіксу, щоб перевірити саме дефект і його виправлення. |
| Small diff і review discipline | `PR-walkthrough.md` описує small diff і шлях issue-to-PR; `REVIEW_NOTES.md` містить зауваження review та їхнє закриття. |

## Top 3 stories

1. **RF-217 inbox ordering:** знайшов причину неправильного порядку refund-запитів, додав regression test, зробив small diff і провів його через review.
2. **Basket calculation refactor:** змінив структуру розрахунку без зміни поведінки; characterization checks залишилися зеленими.
3. **Capstone reproducible demo:** довів core flow до відтворюваного demo та описав запуск у README.

## What not to overstate

- Не називати RF-217 одноосібною розробкою всього рішення: Claude пришвидшував investigation, але scope і фінальне рішення тримав кандидат.
- Не подавати `MIGRATION_PLAN.md` як продакшен-міграцію: це навчальний pilot plan, не доведений до production.
- Не подавати `agents-reviewer.md` як team adoption або усталену платформу: це один поодинокий кейс.
- Не заявляти production-level експертизу в CI/CD, складних integration tests або системній архітектурі: у цих напрямах є поточні прогалини.
- Не розширювати scope історій за межі зафіксованих evidence: підтверджені delivery, тести, small diff, review і characterization checks.

## Current gaps

- CI/CD: немає достатньої підтвердженої практики, щоб позиціонувати це як сильну сторону.
- Складніші integration tests: потрібні додаткові production-подібні приклади.
- Системний рівень архітектури: бракує підтверджених рішень і досвіду відповідного масштабу.
- Масштабування практик на команду: reviewer-agent був одиночним кейсом, а не доведеним team adoption.