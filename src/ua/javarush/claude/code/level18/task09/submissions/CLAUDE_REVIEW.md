# Code Review — task09: empty cart returns 400 on POST /orders

## 1. Методологія та обсяг

- **Предмет рев'ю:** diff останнього коміту гілки `main` — `aca259a` «l18 task09» (законовлений стан task09). Незакомічена зміна робочого дерева — лише `.idea/vcs.xml` (IDE-конфіг, до task09 не стосується, див. Minor m4).
- **Read-only:** продукт-код, тести та PR-опис не змінювались. Рев'ю не вносить правок.
- **Базис перевірки:** `docs/APPROVED_PLAN.md` (approved plan), `PR_DESCRIPTION.md`, focus list лекції (scope creep, missing tests, edge cases, backward compatibility, security-sensitive changes, unclear code).
- **Шкала severity:** Blocker (блокують merge), Major (істотно шкодять якості/верифікації), Minor (зауваження гігієни та стійкості).

## 2. Резюме

**Вердикт: не готово до merge.** Диф змісту не відповідає заявленому в `docs/APPROVED_PLAN.md` контракту (очікується `400`, код повертає `422`), а єдиний продукт-файл `src/main/java/com/example/commerce/orders/OrderController.java` не компілюється: синтаксична помилка в першому рядку та посилання на типи, яких немає ні в дифі, ні в дереві task09. Тест, якого вимагає план, у диф не входить. Крім того, коміт втягує чужий для task09 файл `level18/task08/submissions/PR_DESCRIPTION.md`.

Один блокер сам по собі є фатальним: **код не можна зібрати**, тож жодна з заявлених у `PR_DESCRIPTION.md` поведінок насправді не підтверджується.

## 3. Знайдені проблеми

### Blocker

#### B1. Синтаксична помилка — файл не компілюється
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:1`
- **Деталі:** перший рядок файлу — `/package com.example.commerce.orders;`. Ведучий слеш перетворює декларацію пакета на невалідний Java-код. Такий файл не проходить компіляцію цілком.
- **Чому Blocker:** унеможливлює будь-яку збірку та запуск — ні про тести, ні про роботу ендпоінта не може бути й мови.
- **Звірка з планом:** порушує розділ `## Verification` (docs/APPROVED_PLAN.md:20-21) — цільовий тест фізично не може стати зеленим.

#### B2. Код розходиться з approved plan: 422 замість 400
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:24` (див. також коментар на :22)
- **Деталі:** план фіксує контракт «статус `400`, тіло `Cart is empty`» (`docs/APPROVED_PLAN.md:16-18`), а `PR_DESCRIPTION.md:1` заявляє `400`. Код повертає `HttpStatus.UNPROCESSABLE_ENTITY` (422). Коментар на рядку 22 прямо документує розбіжність («розходиться з approved plan»).
- **Чому Blocker:** це зміна узгодженого зовнішнього контракту без згоди та без відповідного оновлення плану/PR-опису. Клієнти, що очікують `400`, отримають 422; між кодом, планом і описом PR — суперечність.
- **Звірка з планом:** прямопорушення `## Expected response` (docs/APPROVED_PLAN.md:16-18).

#### B3. Посилання на неіснуючі типи — збірка неможлива навіть після виправлення B1
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:14,16,21,26`
- **Деталі:** код залежить від `OrderService` (поле :14, конструктор :16, виклик `orderService.place(request)` :26), `CreateOrderRequest` (параметр :21, доступ `request.items()` :23) і `Order` (:26). Жодного з цих типів немає ні в дифі `aca259a`, ні в усьому дереві task09 (в дереві task09 лише 4 файли: `PR_DESCRIPTION.md`, `docs/APPROVED_PLAN.md`, `OrderController.java`, `submissions/CLAUDE_REVIEW.md`).
- **Чому Blocker:** навіть після усунення B1 файл не збереться — немає типів і немає реалізації бізнес-логіки `place`. Через це заяву з `PR_DESCRIPTION.md` «поведінка для непорожнього кошика не змінюється» неможливо ні перевірити, ні навіть скомпілювати.
- **Звірка з планом:** план передбачає «лише `OrderController` у модулі orders» (docs/APPROVED_PLAN.md:8), тобто припускає існування решти модуля. Якщо модуль мав бути поданий у PR — він відсутній; якщо ні — контролер без залежностей неспроможний.

### Major

#### M1. Відсутні тести, на які посилається план
- **Файл (дефект очікування):** `docs/APPROVED_PLAN.md:20-21` та `PR_DESCRIPTION.md:12` посилаються на цільовий тест `OrderControllerTest.returnsBadRequestForEmptyCart`.
- **Деталі:** у дифі `aca259a` немає жодного тестового файлу; у task09 відсутня структура `src/test`. Тест `returnsBadRequestForEmptyCart` не існує.
- **Чому Major:** верифікація з плану не виконується; регресія не закрита тестом; заява PR «запустити цільовий тест» нездійсненна. Захоплення поведінки «порожній кошик → помилка» нічим не фіксується.

#### M2. Scope creep: у коміт task09 втягнуто файл task08
- **Файл:** `../task08/submissions/PR_DESCRIPTION.md` (за шляхом від кореня: `src/ua/javarush/claude/code/level18/task08/submissions/PR_DESCRIPTION.md`)
- **Деталі:** у коміті `aca259a` заповнено раніше порожній файл task08 (у `e67bcfa` він створений з нульовим розміром) текстом, що відповідає issue-432 task08 (400 + `Cart is empty`). Це чужий для task09 артефакт.
- **Чому Major:** порушує декларований scope «лише `OrderController`» (docs/APPROVED_PLAN.md:8). Зміна іншої задачі в коміті task09 ускладнює рев'ю, історію та, найімовірніше, є випадковим включенням — має бути винесена в окремий коміт/розгалуження або видалена.
- **Звірка з планом:** вихід за межі `## Scope` (docs/APPROVED_PLAN.md:7-9).

#### M3. Верифікаційна команда вказує на модуль `:orders`, якого не існує
- **Файл:** `PR_DESCRIPTION.md:12`, `docs/APPROVED_PLAN.md:20`
- **Деталі:** команда `./gradlew :orders:test --tests OrderControllerTest.returnsBadRequestForEmptyCart` передбачає Gradle-модуль `:orders`. У task09 немає `build.gradle.kts` (на відміну від, напр., `level18/task01|task05|task06|task07`), gradle wrapper і модуля `:orders` у дифі також немає.
- **Чому Major:** інструкція верифікації з плану та PR-опису не виконується в поточному стані репозиторію; процес «план → код → зелена перевірка» обривається.

### Minor

#### m1. Edge case: `null`-тіло запиту
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:21,23`
- **Деталі:** перевірка покриває `items == null` (напр., тіло `{}`), але якщо `@RequestBody` отримає `null`, доступ `request.items()` дасть NPE. Spring MVC зазвичай відхиляє порожнє тіло як `HttpMessageNotReadableException`, тож ризик обмежений, однак поведінка не зафіксована планом.
- **Рекомендація:** захистити `request == null` або явно задекларувати утримання від цього випадку.

#### m2. Невалідні елементи списку поза перевіркою
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:23`
- **Деталі:** `items` з елементами (`[null]`, нульовий або від'ємний `quantity`) проходить перевірку й іде в `orderService.place`. Це свідомо винесено зі scope в `PR_DESCRIPTION.md` «Out of scope», тож формально не дефект, але в ризиках PR не описано, що кошик з такими позиціями не відхиляється.
- **Рекомендація:** зафіксувати це в розділі ризиків `PR_DESCRIPTION.md`, щоб поведінка була очікуваною.

#### m3. Нечіткий контракт успішної відповіді та тип `ResponseEntity<?>`
- **Файл:** `src/main/java/com/example/commerce/orders/OrderController.java:21,26-27`
- **Деталі:** повертається `ResponseEntity<?>` із неіснуючим типом `Order`. `docs/APPROVED_PLAN.md` фіксує лише контракт помилки (`400` / `Cart is empty`), але не специфікує успішну відповідь (пор. з task08, де успіх — `200 OK` + `Order accepted`). Тип-заглушка `<?>` затемнює серіалізаційний контракт.
- **Рекомендація:** закріпити в плані тип і статус успішної відповіді; замінити `ResponseEntity<?>` на конкретний тип.

#### m4. Незакомічений IDE-шум у робочому дереві
- **Файл:** `.idea/vcs.xml` (корінь репозиторію)
- **Деталі:** єдина незакомічена зміна робочого дерева — додавання vcs-маппінгів для level16/level17. До task09 не стосується.
- **Рекомендація:** не включати в PR; тримати конфіг IDE поза змінами або закомітити окремо.

## 4. Звірка з approved plan (`docs/APPROVED_PLAN.md`)

| Пункт плану | Статус | Деталі |
|---|---|---|
| Goal: `POST /orders` з порожнім кошиком → `400 Bad Request` (docs/APPROVED_PLAN.md:4-5) | ❌ Порушено | Код повертає 422 (B2) |
| Scope: лише `OrderController` (docs/APPROVED_PLAN.md:8) | ❌ Порушено | Коміт також торкається `../task08/submissions/PR_DESCRIPTION.md` (M2) |
| Non-goals: не чіпаємо знижки/суму, інші ендпоїнти, нові залежності | ✅ Формально | Знижки/сума не зачіпаються (їх код узагалі не має); нових залежностей немає |
| Expected response: статус `400`, тіло `Cart is empty` (docs/APPROVED_PLAN.md:16-18) | ❌ Порушено | `UNPROCESSABLE_ENTITY` + `Cart is empty` (B2) |
| Verification: `OrderControllerTest.returnsBadRequestForEmptyCart` зелений (docs/APPROVED_PLAN.md:20-21) | ❌ Неможливо | Тесту не існує (M1); код не компілюється (B1, B3) |

**Висновок:** жодна істотна вимога плану не виконана в заявленому вигляді — контракт порушено, верифікація неможлива.

## 5. Покриття focus list лекції

| Фокус | Оцінка | Посилання |
|---|---|---|
| Scope creep | ❌ Присутній | M2: чужий файл task08 у коміті task09 |
| Missing tests | ❌ Присутній | M1: плановий тест не додано |
| Edge cases | ⚠️ Частково | m1 (null-тіло), m2 (невалідні елементи) — не покриті; `items == null` покритий (OrderController.java:23) |
| Backward compatibility | ⚠️ Незабезпечено | B3: контракт успішного шляху не існує/не перевірюваний; B2: контракт помилки змінено без згоди |
| Security-sensitive changes | ✅ Не виявлено | Змін у безпековій площині немає (немає auth, даних, секретів, зовнішніх ресурсів). Єдине суміжне — валідація вводу контролера; вразливостей не виявлено |
| Unclear code | ⚠️ Присутній | B1 (`/package`), B3 (типи-привиди), m3 (`ResponseEntity<?>`), коментар-зізнання про 422 (OrderController.java:22) |

## 6. Висновки для автора

1. **Blocker:** виправити синтаксис (`/package` → `package`), повернути статус `400` згідно з планом, додати (або подати) типи `OrderService` / `CreateOrderRequest` / `Order` — або узгодити зміну плану, якщо контракт дійсно має бути 422.
2. **Major:** додати тест `OrderControllerTest.returnsBadRequestForEmptyCart` (і структуру `src/test` / `build.gradle.kts` для модуля `:orders`), винести файл task08 з коміту task09.
3. **Minor:** зафіксувати edge cases у ризиках PR, специфікувати успішну відповідь, позбутися `ResponseEntity<?>`, виключити `.idea/vcs.xml` з PR.