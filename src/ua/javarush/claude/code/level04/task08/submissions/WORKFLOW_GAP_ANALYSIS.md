# Workflow Gap Analysis — SEARCH-71

## Skipped or weak phases

### 1. Scope validation phase — пропущена повністю
План (AI_IMPLEMENTATION_PLAN.md) не містить жодної перевірки того, чи входять запропоновані зміни в заявлений scope задачі. Розділ "Scope" у плані відсутній. Другий та третій пункти плану прямо називають зміни _"заодно почистити весь модуль"_ та _"підтягнути покращення у CheckoutService"_ — це свідоме розширення, але воно ніде не звіряється з issue.

### 2. Risk assessment phase — пропущена
Жодної оцінки ризиків супутніх змін. Чи може додавання `applyExperimentalDiscount` вплинути на існуючі тести чи бізнес-логіку? Чи може перейменування змінної в `SearchSuggestionService` зламати інші виклики? Ці питання не порушені.

### 3. Test verification phase — виконана слабо (formal only)
План містить рядок _«Перевіряти не обов'язково — зміна проста»_ (AI_IMPLEMENTATION_PLAN.md, рядок 10). Це означає, що фаза верифікації була свідомо пропущена. Наслідок — у тестовому прогоні (test-output.txt) падає `CheckoutServiceTest.calculatesTotalWithoutDiscount`, але це не було виявлено до моменту запуску CI, тому що автор плану вирішив не перевіряти.

### 4. Diff review / scope-boundary audit — відсутня
DIFF.patch показує зміни в трьох файлах, хоча issue.md визначає scope як _«Лише логіка побудови suggestions для порожнього запиту в SearchSuggestionService»_. Жодна фаза не порівняла фінальний diff з визначеним scope.

---

## Evidence

| Observation | File | Lines |
|---|---|---|
| План свідомо виходить за scope: _"Заодно почистити весь модуль search: перейменувати змінні, оновити логування"_ | `inputs/AI_IMPLEMENTATION_PLAN.md` | 5–6 |
| План свідомо виходить за scope: _"Підтягнути кілька покращень у CheckoutService, раз уже відкрито проєкт"_ | `inputs/AI_IMPLEMENTATION_PLAN.md` | 7 |
| Фаза верифікації позначена як необов'язкова | `inputs/AI_IMPLEMENTATION_PLAN.md` | 10 |
| Issue визначає scope: лише `SearchSuggestionService` | `inputs/issue.md` | 13–15 |
| Diff змінює `SearchLogger.java` — файл поза scope | `inputs/DIFF.patch` | 16–24 |
| Diff змінює `CheckoutService.java` — файл поза scope, змінює бізнес-логіку (знижка 3%) | `inputs/DIFF.patch` | 26–35 |
| Перейменування `q` → `normalizedQuery` у `SearchSuggestionService` — зміна, не потрібна для фіксу баги | `inputs/DIFF.patch` | 12 |
| Тест `CheckoutServiceTest.calculatesTotalWithoutDiscount` падає: очікувалося 100.0, отримано 97.0 (через `applyExperimentalDiscount`) | `inputs/test-output.txt` | 6–7 |
| Співвідношення змін: 1 файл у scope (`SearchSuggestionService`), 2 файли поза scope — 66% diff не стосується задачі | `inputs/DIFF.patch` | весь файл |

---

## Primary risk

**Uncontrolled scope creep, що ламає суміжну функціональність.** Стороння зміна в `CheckoutService` (`applyExperimentalDiscount`) не тільки виходить за межі SEARCH-71, але й активно ламає існуючий тест. Якби цей diff потрапив у production, покупці отримували б неочікувану знижку 3% на всі замовлення — фінансовий та репутаційний дефект. Додатково, зміна `SearchLogger` та перейменування змінної ускладнюють code review та збільшують імовірність конфлікту при злитті паралельних гілок.

---

## Recommended correction

**Не продовжувати edits поверх поточного diff.** Зафіксувати помилки процесу — потім виправити код.

1. **Відкотити diff** — повернути всі три файли до стану до змін. Жодних часткових виправлень.
2. **Оновити CI-воркфлоу**, щоб він включав фазу **scope validation** перед генерацією коду:
      - Вичитати issue.md, виписати дозволені файли та обмеження.
      - Якщо план пропонує зміни поза ними — повернути на доопрацювання.
3. **Додати фазу risk assessment** у план: перш ніж додавати будь-яку зміну, що не вказана в issue, оцінити вплив на тести та суміжні модулі. Зміни поза scope мають проходити окреме узгодження (або відхилятися).
4. **Зробити фазу test verification обов'язковою**: жоден PR не може бути завершений без прогону тестів. Якщо тести падають — diff не приймається.
5. **Після виправлення процесу** — згенерувати новий diff, який:
      - змінює **лише** `SearchSuggestionService.java`,
      - додає **лише** перевірку порожнього запиту (null/isBlank → `List.of()`),
      - **не** перейменовує змінні,
      - **не** чіпає `SearchLogger.java`,
      - **не** чіпає `CheckoutService.java`.
6. **Прогнати тести** — переконатися, що `SearchSuggestionServiceTest.emptyQueryReturnsNoSuggestions` проходить, а `CheckoutServiceTest` не регресував.
