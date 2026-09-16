# Migration Validation Report

## Checks run

- **Build:** виконано `./gradlew clean build`; результат — `BUILD SUCCESSFUL`, усі 8 actionable tasks виконано.
- **Tests:** виконано `./gradlew test --tests '*reports*'`; пройдено 2 тести, 0 помилок:
  - `MonthlyReportJsonShapeTest > notesPresentEvenWhenNull()`;
  - `MonthlyReportContractTest > monthlyReportMatchesBaseline()`.
- **Smoke/contract check:** виконано запит `GET /api/reports/monthly?period=2026-04`; endpoint повернув HTTP 200. Вивід порівняно з baseline через `diff -u`; відмінностей не виявлено.
- **PDF smoke/manual check:** ручна звірка monthly report у PDF підтвердила відповідність шапки, періоду, підсумкових сум і таблиці категорій JSON-відповіді.

## Feature-parity evidence

Baseline та pilot artifacts порівнювалися кількома способами:

1. JSON-відповідь pilot endpoint збережено у `/tmp/pilot-monthly.json` і порівняно з `evidence/baseline-monthly.json` побайтовим unified diff через `diff -u`; diff порожній.
2. Автоматичний контрактний тест `MonthlyReportContractTest > monthlyReportMatchesBaseline()` підтвердив збіг із baseline.
3. JSON shape test підтвердив наявність поля `notes`, навіть коли його значення `null`.
4. Ручна перевірка PDF зіставила заголовок, період, підсумкові суми та порядок рядків категорій із JSON response.

За перевіреним сценарієм `2026-04` у валюті EUR pilot зберігає функціональну та структурну паритетність із baseline: `totalIncome`, `totalExpense`, `net`, категорії та `notes` збігаються.

## Failures and fixes

- Під час зафіксованих build, report-тестів, контрактної перевірки та smoke check невдалих перевірок не було.
- Застосування виправлень не знадобилося: build завершився успішно, 2 report-тести пройшли, endpoint повернув 200, а unified diff не показав розбіжностей.
- Окремих помилок PDF-перевірка також не зафіксувала.

## Known limitations

- CSV-експорт monthly report не перевірявся, оскільки він поза scope цієї pilot-ітерації.
- Локалізація чисел для валют, відмінних від EUR, не перевірялася; ручна звірка виконана лише для EUR.
- PDF перевірявся вручну, без окремого автоматизованого snapshot або pixel-diff тесту.
- Підтвердження parity охоплює період `2026-04` і надані baseline/pilot artifacts, а не весь набір можливих періодів та даних.

## Remaining risks

- Неперевірений CSV-шлях може містити розбіжності, які не видно в JSON endpoint або PDF.
- Поведінка форматування сум для не-EUR валют може відрізнятися після міграції та вплинути на користувацький експорт.
- Ручна PDF-звірка може не виявити рідкісні регресії верстки або відмінності на інших наборах даних.
- Smoke check підтверджує доступність endpoint лише в перевіреному середовищі та для одного запиту; він не підтверджує стабільність під навантаженням або для інших періодів.

## Recommendation

**continue pilot** — перевірений JSON/contract шлях демонструє parity і всі наявні автоматизовані checks проходять, але перед повним rollout потрібно окремо перевірити CSV-експорт, не-EUR локалізацію та розширити coverage за періодами й наборами даних.