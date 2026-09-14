# Сигнали технічного боргу

## Область аналізу та джерела доказів

- **Область:** `task04` — `/Users/romanmakarenko/javarush/6512114/javarush-project/src/ua/javarush/claude/code/level26/task04`.
- **Історія:** виконано `bash init.sh` з каталогу `task04`. Оскільки `task04/.git` до запуску не існував, скрипт створив вкладений репозиторій із п’ятьма комітами `c1`–`c5`; зовнішня історія репозиторію до неї не домішується.
- **Churn:** `git log --format= --name-only | grep -v '^$' | sort | uniq -c | sort -rn` у вкладеному репозиторії.
- **Coverage:** `gradle jacocoTestReport`.
- **Звіти:** `build/reports/jacoco/test/html/index.html` та `build/reports/jacoco/test/jacocoTestReport.xml`.
- Усі числові значення coverage нижче — фактичні значення з XML-звіту для цього запуску, а не оцінки.

## Реєстр сигналів

| ID | Сигнал | Area | Evidence | Risk | Suggested next step |
|---|---|---|---|---|---|
| DS-01 | `computeMrr` поєднує proration і churn | Design / separation of concerns | `legacy/com/cashflow/mrr/MrrEngine.java:11-18` — FIXME прямо вказує на змішування двох відповідальностей; метод підсумовує підписки й віднімає `churnAdjustment`. | Зміна одного правила може ненавмисно змінити інше. Для фінансової логіки це підвищує regression risk і ускладнює локалізацію помилки. | Розділити proration і churn на окремі операції після фіксації поточного контракту та characterization-тестів. |
| DS-02 | Не визначена поведінка для від’ємного `churnAdjustment` | Domain correctness / input contract | `legacy/com/cashflow/mrr/MrrEngine.java:17` — TODO про від’ємне коригування та невизначену поведінку. | Уточнення знака або валідації може змінити розрахунок MRR для граничних вхідних даних; без контракту помилка може тихо вплинути на грошові суми. |
| DS-03 | `MrrEngine` є історичним hotspot | Change hotspot | `legacy/com/cashflow/mrr/MrrEngine.java:5-7` — Javadoc називає його найстарішою та найбільш часто виправлюваною частиною; фактичний churn: **5** згадувань файла проти **1** для кожного іншого файла у виводі нижче. | Часті зміни збільшують накопичений regression exposure. Наступні правки цього файла потребують малих ізольованих змін, посиленого review та перевірки суміжних правил. |
| DS-04 | Ключова логіка `computeMrr` майже не має тестового захисту | Testability / coverage | `src/test/java/com/cashflow/mrr/MrrEngineTest.java:7-8` прямо каже, що `computeMrr` не покритий; єдиний тест у `:12-16` перевіряє лише `annualize`. JaCoCo для `MrrEngine`: line **2/7 (28.6%)**, branch **0/2 (0.0%)**. | Рефакторинг або виправлення hotspot не має достатнього safety net: зміни можуть пройти CI без перевірки сценаріїв обчислення MRR. |
| DS-05 | `round2` дублює логіку округлення billing-коду | Duplication / monetary consistency | `legacy/com/cashflow/mrr/LegacyUtils.java:7-10` — TODO вказує на дублювання з `InvoiceService`; `round2` реалізує округлення через `Math.round(value * 100.0) / 100.0`. | Виправлення в одному місці може залишити інше зі старою поведінкою, утворивши різні invoice totals. |
| DS-06 | `LegacyUtils.safe` має ширший public API, ніж потрібно | API surface / cleanup | `legacy/com/cashflow/mrr/LegacyUtils.java:12-15` — FIXME зазначає, що метод використовується лише в одному місці, але є `public`. Read-only пошук `LegacyUtils\.safe|safe\(` у snapshot знайшов лише декларацію в `LegacyUtils.java:14`. | Зменшення visibility або видалення helper може бути безпечним лише після перевірки споживачів. Наявність публічного API збільшує surface area майбутніх змін і ціну сумісності. |
| DS-07 | Округлення нібито дублюється у трьох місцях `InvoiceService` | Duplication / billing calculation | `src/main/java/com/cashflow/billing/InvoiceService.java:11` — TODO повідомляє про три дублікати й пропонує винести helper; додаткові реалізації в межах цього snapshot не знайдені. | Будь-яке виправлення rounding rule потребує синхронних змін. Розбіжність між місцями може змінити підсумок рахунка. |
| DS-08 | `region == null` призводить до NPE | Input validation / robustness | `src/main/java/com/cashflow/billing/InvoiceService.java:17-19` — FIXME фіксує NPE; `isTaxable` викликає `region.equalsIgnoreCase("EXEMPT")` без null-перевірки. | Некоректний input на billing boundary стає runtime failure замість контрольованої відповіді, що створює ризик недоступності операції виставлення рахунка. |
| DS-09 | Податкові ставки захардкоджені в коді | Configuration / business policy | `src/main/java/com/cashflow/billing/TaxCalculator.java:5,9-19` — Javadoc називає підхід застарілим, TODO вимагає конфігурацію; ставки `EU=0.20`, `US=0.07`, інші регіони `0.00`. | Зміна політики або ставки вимагає code deployment і може бути пропущена чи виконана непослідовно. Це підвищує ризик неправильних рахунків під час policy changes. |

## Деталі вимірювань

### Git churn

Фактичний результат команди `git log --format= --name-only | grep -v '^$' | sort | uniq -c | sort -rn`:

```text
5 legacy/com/cashflow/mrr/MrrEngine.java
1 src/test/java/com/cashflow/mrr/MrrEngineTest.java
1 src/main/java/com/cashflow/billing/TaxCalculator.java
1 src/main/java/com/cashflow/billing/InvoiceService.java
1 legacy/com/cashflow/mrr/LegacyUtils.java
1 init.sh
1 docs/DEBT_SIGNALS.md
1 build.gradle
```

Команда `git log --follow --oneline -- legacy/com/cashflow/mrr/MrrEngine.java` показала п’ять комітів:

```text
4acd37b docs: flag proration/churn debt in computeMrr
8a57c12 docs: note MrrEngine as oldest hot path
350c28e feat: add annualize to MrrEngine
de2bed0 feat: subtract churnAdjustment in computeMrr
16c3a3d feat: initial CashFlow Dashboard skeleton
```

### JaCoCo та тести

`gradle jacocoTestReport` завершився успішно: **BUILD SUCCESSFUL**, один тест пройшов (`tests=1`, `failures=0`, `errors=0`, `skipped=0`). Звіт містить такі aggregate counters:

| Counter | Covered | Missed | Total | Coverage |
|---|---:|---:|---:|---:|
| INSTRUCTION | 10 | 97 | 107 | 9.3% |
| BRANCH | 0 | 10 | 10 | 0.0% |
| LINE | 2 | 20 | 22 | 9.1% |
| METHOD | 2 | 9 | 11 | 18.2% |
| CLASS | 1 | 3 | 4 | 25.0% |

Покласові line counters:

| Клас | Covered lines | Total lines | Coverage |
|---|---:|---:|---:|
| `com/cashflow/mrr/MrrEngine` | 2 | 7 | 28.6% |
| `com/cashflow/mrr/LegacyUtils` | 0 | 3 | 0.0% |
| `com/cashflow/billing/TaxCalculator` | 0 | 7 | 0.0% |
| `com/cashflow/billing/InvoiceService` | 0 | 5 | 0.0% |

Артефакти цього запуску:

- `build/reports/jacoco/test/html/index.html`
- `build/reports/jacoco/test/jacocoTestReport.xml`
- `build/test-results/test/TEST-com.cashflow.mrr.MrrEngineTest.xml`

## Caveats

- TODO/FIXME та Javadoc — це сигнали технічного боргу й наміри авторів, а не самі по собі доказ production defect.
- Coverage описує лише поточний snapshot і єдиний наявний тест; низьке покриття показує слабку перевірку, але не доводить фактичну частоту runtime-помилок.
- `init.sh` створив вкладений репозиторій у `task04`; його п’ять комітів — окреме джерело churn і не є історією зовнішнього репозиторію.
- Churn рахує згадки файла в історії, а не розмір diff і не тяжкість змін. `MrrEngine` має найбільший count, але це не доводить причинність дефектів.
- Твердження про «три місця» округлення та «одне використання» `safe` походять із TODO/FIXME і пошуку в цьому snapshot; зовнішні споживачі або інші репозиторії не перевірялися.
- `build/` і звіти JaCoCo є згенерованими артефактами вимірювання, а не частиною вихідного коду чи debt evidence.