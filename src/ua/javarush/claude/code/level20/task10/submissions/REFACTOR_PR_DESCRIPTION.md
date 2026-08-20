# REFACTOR PR — OrderService → OrderEventPublisher

Архітектурний refactor: інфраструктурний side effect (надсилання події замовлення в Kafka)
виноситься з `OrderService` у виділений collaborator `OrderEventPublisher`.
Це чисто структурна зміна — без нової функціональності, виправлень чи міграцій.

## Goal

- Зменшити відповідальність `OrderService`: залишити в сервісі оркестрацію (збереження замовлення
  і делегування публікації), а фактичне надсилання події винести у `OrderEventPublisher`.
- Розділити доменну логіку та інфраструктуру (Kafka/`KafkaTemplate`), не змінюючи
  зовнішнього контракту сервісу.

## Scope

**В межах refactor:**

- `OrderService` — `createOrder(...)` тепер делегує публікацію події collaborator-у замість
  inline-надсилання.
- `OrderEventPublisher` (новий `@Component`) — володіє `KafkaTemplate`, topic `orders.created.v1`
  та формуванням `OrderCreatedEvent`.
- `OrderServiceTest` — оновлено під перевірку делегування (`verify publishOrderCreated`).

**Поза межами:**

- Без нових feature, без bugfix, без міграцій даних/схеми/формату повідомлень.

## Behavior preserved

- **Public API**:
  - `OrderService.createOrder(NewOrder newOrder) : Order` — сигнатура, поведінка та повернене
    значення без змін.
  - Вхідний контракт `NewOrder(customerId, amount)` і сутність
    `Order(id, customerId, amount)` — без змін.
  - Спосіб створення/використання сервісу (конструктор, ін'єкція залежностей) — без змін.
- **Event contract**:
  - Payload події `OrderCreatedEvent(orderId, customerId, amount)` — без змін.
  - Topic `orders.created.v1` — без змін.
  - Ключ повідомлення (order id) — без змін.
  - Порядок операцій збережено: спершу `repository.save(...)`; лише після успішного збереження
    публікується подія.

## Checks run

Перевірка PR виконується через Gradle-тести модуля (JUnit Platform, згідно
`tasks.test { useJUnitPlatform() }` у `build.gradle.kts`):

```bash
gradle test
```

Вибірково, лише тест refactor-у:

```bash
gradle test --tests com.example.orders.OrderServiceTest
```

Це реальні test-команди проєкту: вони запускають тестовий сьют на JUnit 5
(`src/test/java/com/example/orders/OrderServiceTest.java`, AssertJ + Mockito).

## Acceptance criteria

- [ ] **tests/checks green** — `gradle test` завершується успішно: усі тести модуля, включно
      з `OrderServiceTest`, проходять без помилок.
- [ ] **no accidental dependency changes** — у `build.gradle.kts` та будь-яких файлах залежностей
      немає змін: жодна бібліотека/плагін не доданий, не видалений і не оновлений.
- [ ] **rollback possible** — відкат до попереднього коміту повністю відновлює попередню
      поведінку; refactor не потребує міграцій схеми, даних чи зміни формату повідомлень,
      тому відкат не має побічних наслідків.
- [ ] Public API `OrderService.createOrder(NewOrder)` не змінений — сигнатура і повернений
      тип `Order` ті самі.
- [ ] Event contract не змінений — payload `OrderCreatedEvent(orderId, customerId, amount)`
      та topic `orders.created.v1` ідентичні попереднім.
- [ ] Послідовність операцій збережена — подія публікується лише після успішного
      `repository.save(...)`.
- [ ] Diff містить лише рефакторинг: немає нових фіч, виправлень багів чи міграційної роботи.
- [ ] Оновлений `OrderServiceTest` підтверджує делегування collaborator-у
      (`verify(eventPublisher).publishOrderCreated(...)`).