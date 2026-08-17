# Класифікація запропонованих змін: refactor чи ні

Цей документ класифікує кожен пункт з `inputs/change-requests.md` за межею
refactor / non-refactor. Жодних правок коду тут не пропонується — лише аналіз.

## Session commands used

Аналіз виконано через Claude CLI у режимі read-only (жодних edits):

- `git status` / `git log` — перевірка стану робочої директорії (`fd68236 level20 t04`),
  підтвердження, що жодних локальних змін до аналізу не було.
- `ls` / `find` — інвентаризація файлів: `inputs/change-requests.md`,
  `src/main/java/com/example/orders/{OrderController,OrderRequest,OrderResponse,OrderService}.java`,
  `submissions/REFACTOR_CLASSIFICATION.md` (шаблон).
- Read `inputs/change-requests.md` та всіх чотирьох класів у `src` — read-only зчитування вхідних даних і коду.
- Показ permission posture: перегляд налаштувань permissions
  (`~/.claude/settings.json`, `.claude/settings.local.json`). Зафіксовано режим аналізу
  без правок: в permissions дозволено лише `Read(//Users/romanmakarenko/.claude/plugins/**)`;
  жодного allow на Edit/Write коду — тобто будь-яка зміна потребувала б явного підтвердження,
  а для класифікації правки не виконувались.

## Класифікація

1. Перейменувати private-метод `processOrderChecks` на `validateOrderRequest` та оновити виклик у `placeOrder`.
   - Мітка: **behavior-preserving** (refactor)
   - Обґрунтування: `processOrderChecks` — private-метод, єдиний виклик — внутрішній
     з `placeOrder`. Він не входить у публічний контракт сервісу: зовнішній клієнт не
     може його інвокувати, тож перейменування не видиме назовні. Тіло не чіпається,
     порядок і тексти перевірок ті самі — всі зовнішньо спостережувані ефекти
     (`IllegalArgumentException` з тими самими повідомленнями, той самий `OrderResponse`
     зі статусом `"PLACED"`) зберігаються повністю.

2. У `processOrderChecks` поміняти місцями перевірки: спочатку `amount`, потім `orderId`.
   - Мітка: **changes-behavior** (не refactor)
   - Обґрунтування: тексти умов і повідомлень не змінюються, але змінюється *яке саме*
     повідомлення отримає клієнт для запиту, що порушує обидві перевівки одночасно.
     Наприклад, запит з `orderId == null` і `amount <= 0` зараз кидає
     `"Order id must not be blank"`, а після перестановки — `"Amount must be positive"`.
     Оскільки тексти винятків потрапляють у відповідь REST-ендпоінта `POST /api/orders`,
     це зовнішньо спостережувана зміна вмісту помилки для клієнтів.

3. У `cancelOrder` замінити статус `"CANCELLED"` на `"CANCELED"` (одна `L`).
   - Мітка: **changes-behavior** (не refactor)
   - Обґрунтування: рядок статусу кладеться в `OrderResponse`, який повертається з
     `POST /api/orders/{orderId}/cancel`. Поле `status` у JSON-відповіді — частина
     зовнішнього контракту API; клієнт, який порівнює `status` з `"CANCELLED"`,
     після зміни побачить інше значення. Це зміна зовнішньо спостережуваного вмісту відповіді.

4. У `findOrder` винести текст `"Order not found: " + orderId` у локальну змінну `notFoundMessage`.
   - Мітка: **behavior-preserving** (refactor)
   - Обґрунтування: текст повідомлення та тип винятку (`IllegalArgumentException`)
     залишаються без змін — як і значення `orderId`, що підставляється в рядок. Локальна
     змінна — внутрішня деталь реалізації, невидима назовні: клієнт `GET /api/orders/{orderId}`
     для неіснуючого замовлення отримає ту саму помилку, що й раніше.

5. Перейменувати public-метод `findOrder` на `getOrder` та оновити виклик у `OrderController.getOrder`.
   - Мітка: **changes-behavior** (не refactor)
   - Обґрунтування: `OrderService` — публічний клас, а `findOrder` — його публічний метод,
     частина контракту service-шару. Будь-який зовнішній споживач (інший компонент, тест,
     клієнт, що інвокує бін) викликає `findOrder(...)`; після перейменування такі виклики
     перестають компілюватися/працювати. Хоча внутрішній виклик у `OrderController`
     оновлюється, сама зміна імені публічного API є зовнішньо спостережуваною
     (breaking) для всіх, хто звертається до сервісу напряму.

## Підсумок

- **1** — перейменування private-методу `processOrderChecks` → `validateOrderRequest`: `behavior-preserving`.
- **2** — зміна порядку перевірок у `processOrderChecks`: `changes-behavior`.
- **3** — статус `"CANCELLED"` → `"CANCELED"` у відповіді: `changes-behavior`.
- **4** — винесення `notFoundMessage` у локальну змінну: `behavior-preserving`.
- **5** — перейменування public-методу `findOrder` → `getOrder`: `changes-behavior`.

Candidates, що змінюють зовнішньо спостережувану поведінку: 2, 3, 5.
Candidates, що є behavior-preserving refactor: 1, 4.