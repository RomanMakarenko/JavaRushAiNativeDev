# Baseline characterization tests — `mrr-engine`

## Призначення

Цей документ фіксує current behavior модуля `mrr-engine`, який обчислює Monthly Recurring Revenue (MRR) за підписками CashFlow Dashboard. Baseline підготовлено перед modernization. Він описує спостережувані результати, типи наявних checks і compatibility-залежності; код застосунку та тести не змінюються.

## Зафіксовані сценарії

### 1. Refund у поточному місяці

- **Level:** висока business criticality, високий change risk.
- **Inputs:** refund оформлено в поточному розрахунковому місяці.
- **Observable outputs:** `mrr[current]`, `mrr[next]`.
- **Known quirks:** refund у поточному місяці не зменшує `mrr[current]`, а впливає на `mrr[next]` через carry-forward. Це відома небажана legacy-поведінка, яку очікує downstream `Billing reconciliation job`; її зміна без погодження може зламати звірку.
- **Evidence:** `inputs/BEHAVIOR_INVENTORY.md` (Flow 1); `inputs/RISK_MAP.md` (refund carry-forward); `inputs/downstream-notes.md` (Billing reconciliation job); characterization check `RefundCarryForwardCharacterizationTest` — зелений у `inputs/existing-tests.md`.

### 2. Plan switch (upgrade / downgrade)

- **Level:** висока business criticality, середній change risk.
- **Inputs:** підписник змінює тариф у межах місяця; дата зміни визначає період proration.
- **Observable outputs:** `mrr[current]`, рядок `planId` у snapshot.
- **Known quirks:** граничні дати зміни тарифу всередині місяця залишаються unknown у risk map; compatibility для `Analytics export` вважається прийнятною, оскільки розбіжності згладжуються агрегацією.
- **Evidence:** `inputs/BEHAVIOR_INVENTORY.md` (Flow 2); `inputs/RISK_MAP.md` (plan switch); `inputs/downstream-notes.md` (Analytics export); unit check `PlanSwitchProrationTest` — зелений у `inputs/existing-tests.md`.

### 3. Pause / resume в одному місяці

- **Level:** середня business criticality, середній change risk.
- **Inputs:** підписку ставлять на паузу і знімають із паузи (resume) в межах одного місяця.
- **Observable outputs:** `mrr[current]`.
- **Known quirks:** при pause і resume в одному місяці MRR не обнуляється; нарахування зберігається як за повний місяць. Автоматизований characterization check відсутній — це біла пляма baseline.
- **Evidence:** `inputs/BEHAVIOR_INVENTORY.md` (Flow 3); `inputs/RISK_MAP.md` (pause / resume); `inputs/existing-tests.md` (сценарій відсутній, закривається ручною перевіркою).

### 4. Генерація місячного snapshot

- **Level:** висока business criticality, високий change risk.
- **Inputs:** формування місячного MRR-звіту.
- **Observable outputs:** JSON snapshot звіту, зокрема поля `generatedAt`, `traceId` і порядок планів.
- **Known quirks:** `generatedAt` і `traceId` нестабільні; порядок планів не гарантований. Через це golden master нестабільний і потребує нормалізації. `Finance dashboard` не спирається на `generatedAt` і `traceId`, але падає, якщо порядок планів між прогонами змінюється.
- **Evidence:** `inputs/BEHAVIOR_INVENTORY.md` (Flow 4); `inputs/RISK_MAP.md` (snapshot); `inputs/downstream-notes.md` (Finance dashboard); golden-master check `MrrGoldenMasterTest` — нестабільний у `inputs/existing-tests.md`.

## Current behavior vs desired behavior

| Сценарій | Current behavior | Desired behavior / modernization boundary |
|---|---|---|
| Refund у поточному місяці | Refund переносить вплив на `mrr[next]`, не зменшуючи `mrr[current]`. | Бажано переглянути carry-forward, але цю compatibility-поведінку залишено **поза поточним modernization scope**: спочатку потрібні погодження і міграція `Billing reconciliation job`. |
| Plan switch | MRR перераховується пропорційно від дати зміни. | У поточному scope зберігається proration; окремо потребують уточнення граничні дати всередині місяця. |
| Pause / resume | Pause → resume в одному місяці зберігає повний місяць нарахування. | У поточному scope поведінка не змінюється; спочатку потрібен characterization check замість наявної ручної перевірки. |
| Місячний snapshot | Поля `generatedAt`, `traceId` нестабільні, порядок планів не гарантований. | Для надійного порівняння потрібна нормалізація нестабільних полів і порядку; цей baseline не виконує таку зміну і не виправляє current behavior. |

## Manual verification

Manual verification потрібна для сценарію **pause / resume в одному місяці**, оскільки автоматизований check у поточному наборі тестів відсутній.

1. Взяти підписку та розрахунковий місяць.
2. Зафіксувати MRR до pause.
3. Виконати pause, а потім resume в межах того самого місяця.
4. Сформувати MRR за місяць і перевірити `mrr[current]`.
5. Зафіксувати результат: згідно з behavior inventory, MRR не обнуляється і нарахування зберігається як за повний місяць.

Ця процедура є ручною перевіркою поточної поведінки, а не виправленням або доповненням application/test code.

## Downstream compatibility

- `Billing reconciliation job` очікує, що refund поточного місяця відображається в MRR наступного місяця.
- `Finance dashboard` споживає місячний JSON snapshot: не залежить від `generatedAt` і `traceId`, але чутливий до зміни порядку планів.
- `Analytics export` використовує `mrr[current]`; proration при plan switch прийнятна, а розбіжності згладжуються агрегацією.

## Обмеження baseline

- Усі твердження вище взято з матеріалів у `inputs/`; baseline фіксує current behavior, а не бажаний кінцевий дизайн.
- Відомі quirks не виправляються в цьому документі.
- Код застосунку і тести не змінювалися.