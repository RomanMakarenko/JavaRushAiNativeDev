# Agent Pipeline Diagram — ORD-548

## Chosen pattern

**Pipelined Extraction with Boundary Gate (Pipeline)**

Обґрунтування: задача структурна, ризик scope creep високий, контракт публічного URL
недоторканний. Послідовний pipeline із чіткою межею (gate) на вході й виході зменшує
ризик розповзання змін у сусідні області (`payments/`, `support/`).

```
  ┌──────────┐     ┌──────────────┐     ┌──────────────────────┐
  │          │     │              │     │    BOUNDARY VERIFIER │
  │ ARCHITECT│────>│ IMPLEMENTER  │────>│   ┌────────────────┐ │
  │  (plan)  │     │  (subagent)  │     │   │   fresh session│ │
  │          │     │              │     │   │   5 checklists  │ │
  └──────────┘     └──────────────┘     │   │   ALL(passed)?  │ │
                                        │   │   => PASS/FAIL  │ │
                                        │   └──────┬─────────┘ │
                                        └──────────┼───────────┘
                                                   │ PASS → done
                                                   │ FAIL → back to Architect
```

## Roles

Ролі виконуються послідовно. Кожна наступна отримує артефакт попередньої. Для
кожної ролі зазначено реалізацію з переліку: `plan mode`, `subagent`, `fresh session`, `human`.

### 1. Architect — реалізація: `plan mode`

**Що робить:**
- Аналізує поточний `OrderExportController` — яка CSV-логіка в ньому.
- Визначає межу модуля `orders/export`:
  - які класи туди переїжджають,
  - який інтерфейс `ExportService` (методи, сигнатура),
  - що залишається в контролері (лише прийом запиту й делегування).
- Фіксує контракт: URL `/api/orders/export` не змінюється, формат відповіді
  не змінюється.
- Описує scope lock: не заходити в `payments/`, `support/`, не рефакторити суміжне.

### 2. Implementer — реалізація: `subagent`

**Що робить:**
- Створює пакет `orders/export`.
- Створює `ExportService` згідно з контрактом від Architect.
- Переносить CSV-логіку з контролера в `ExportService`.
- Спрощує контролер до делегування виклику (request -> service -> response).
- Запускає тести: `./gradlew test --tests "*OrderExport*"`.
- Не виходить за межі, визначені в `EXTRACTION_PLAN.md`.

### 3. Boundary Verifier — реалізація: `fresh session`

**Що робить:**
- Незалежна перевірка (свіжа сесія — без контексту виконавця):
  1. Чи жоден файл у `payments/` не змінено?
  2. Чи жоден файл у `support/` не змінено?
  3. Чи публічний URL `/api/orders/export` залишився без змін?
  4. Чи існує модуль `orders/export` з очікуваною структурою?
  5. Чи `./gradlew test --tests "*OrderExport*"` проходить?
- **Фінальний крок (логічний вентиль):**
  - якщо всі 5 критеріїв passed -> **PASS** (pipeline завершено)
  - якщо хоч один failed -> **FAIL** з поверненням до Architect

## Artifacts

| № | Від ролі | До ролі | Артефакт | Вміст |
|---|---|---|---|---|
| 1 | Architect | Implementer | EXTRACTION_PLAN.md | boundary map, контракт інтерфейсу, scope lock |
| 2 | Implementer | Boundary Verifier | Change set + test log | diff змінених файлів + лог тестів |

Boundary Verifier не передає артефакт далі — він або завершує pipeline (PASS),
або повертає VERIFICATION_REPORT.md назад до Architect для виправлення (FAIL + loop).

## Verifier termination condition

Machine-checkable умова (кон'юнкція всіх п'яти):

```
ALL(
  payments/ — жоден файл не змінено,
  support/ — жоден файл не змінено,
  /api/orders/export — контракт URL збережено,
  module orders/export — створено з ExportService,
  ./gradlew test --tests "*OrderExport*" — passed
)
```

Умова перевіряється автоматично за допомогою `git diff --name-only` (критерії 1-2),
інспекції контролера (критерій 3), перевірки існування пакета (критерій 4) та
результату тестового запуску (критерій 5). Якщо ALL(...) = true -> PASS.
Інакше -> FAIL з поверненням до Architect.

## Human decision

Єдине місце, де потрібна людина — **затвердження boundary map на етапі Architect**.
Якщо межа модуля неочевидна (наприклад, CSV-допоміжний клас використовується і в
`orders/`, і в `payments/`), Architect фіксує альтернативи, а людина вирішує,
чи розширити scope, чи залишити копію в старому місці.

Якщо Boundary Verifier повертає FAIL через неоднозначний критерій (наприклад, машина
показала зміну в `payments/`, але це лише імпорт, який з'явився через автоматичне
форматування), людина перевіряє й виносить фінальний вердикт — це другий potential
human decision point, але для ORD-548 малоймовірно.

Решта pipeline повністю автоматична: plan mode, subagent i fresh session
не потребують людини.