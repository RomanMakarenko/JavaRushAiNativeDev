# RUNTIME FLOW — Place Order

## Entry point

```
POST /api/orders
Content-Type: application/json
```

**File:** [`OrderController.java`](../src/main/java/com/acme/orders/OrderController.java), line 20–25 — метод `placeOrder()`.

Spring MVC десеріалізує JSON-тіло запиту в record [`OrderRequest`](../src/main/java/com/acme/orders/OrderRequest.java) (sku, quantity, paymentToken) і передає в контролер.

---

## Call chain

### 1. OrderController.placeOrder(OrderRequest)

**File:** [`OrderController.java`](../src/main/java/com/acme/orders/OrderController.java), line 21–24

```java
OrderResult result = orderService.placeOrder(request);
return ResponseEntity.ok(result);
```

Контролер не містить ніякої логіки — вся бізнес-логіка делегується в сервіс.

---

### 2. OrderService.placeOrder(OrderRequest)

**File:** [`OrderService.java`](../src/main/java/com/acme/orders/OrderService.java), line 19–28

```java
public OrderResult placeOrder(OrderRequest request) {
    if (request.quantity() > maxQuantity) {
        throw new IllegalArgumentException("Quantity exceeds limit: " + maxQuantity);
    }
    OrderEntity entity = new OrderEntity(request.sku(), request.quantity(), "CONFIRMED");
    OrderEntity saved = orderRepository.save(entity);
    return new OrderResult(saved.status());
}
```

Сервіс виконує три кроки:

#### 2a. Валідація quantity (line 21–23)

Перевіряє, чи `request.quantity()` не перевищує `maxQuantity`.

#### 2b. Створення OrderEntity (line 25)

Мапить `OrderRequest` → `OrderEntity`. Поле `status` жорстко закодоване як `"CONFIRMED"`.

#### 2c. Збереження через OrderRepository (line 26)

**File:** [`OrderRepository.java`](../src/main/java/com/acme/orders/OrderRepository.java), line 14–17

```java
public OrderEntity save(OrderEntity entity) {
    long id = sequence.incrementAndGet();
    store.put(id, entity);
    return entity;
}
```

- Генерація ID через `AtomicLong.sequence.incrementAndGet()`
- Зберігання в `ConcurrentHashMap<Long, OrderEntity>.store`
- Повертає той самий об'єкт (ID не записується в `OrderEntity`)

#### 2d. Формування відповіді (line 27)

`new OrderResult(saved.status())` — повертає `"CONFIRMED"`.

---

## Data transformations

| Крок | Тип | Дані |
|------|-----|------|
| HTTP → Controller | JSON | `{"sku":"SKU-1","quantity":2,"paymentToken":"tok-test"}` |
| Controller → Service | `OrderRequest` (record) | `sku="SKU-1"`, `quantity=2`, `paymentToken="tok-test"` |
| Service → Repository | `OrderEntity` (record) | `sku="SKU-1"`, `quantity=2`, `status="CONFIRMED"` |
| Repository — storage | `ConcurrentHashMap<Long, OrderEntity>` | `1 → OrderEntity[sku=SKU-1, qty=2, status=CONFIRMED]` |
| Service → Controller | `OrderResult` (record) | `status="CONFIRMED"` |
| Controller → HTTP | JSON | `{"status":"CONFIRMED"}` |

**Ключове:** `paymentToken` губиться на етапі OrderService (не мапиться в `OrderEntity`). ID замовлення, згенерований `OrderRepository`, не повертається клієнту.

---

## Error paths

### E1. Quantity exceeds limit

| | |
|---|---|
| **Condition** | `request.quantity() > maxQuantity` |
| **File** | [`OrderService.java`](../src/main/java/com/acme/orders/OrderService.java), line 21–23 |
| **Exception** | `IllegalArgumentException("Quantity exceeds limit: N")` |
| **HTTP result** | **500 Internal Server Error** (дефолтна поведінка Spring — відсутній `@ExceptionHandler`) |
| **Note** | Валідаційна помилка мала б повертати **400 Bad Request**, а не 500 |

### E2. Malformed / invalid request body

| | |
|---|---|
| **Condition** | JSON не відповідає структурі `OrderRequest` (невірний тип, відсутнє поле тощо) |
| **File** | Spring MVC — автоматична десеріалізація `@RequestBody` |
| **HTTP result** | **400 Bad Request** |
| **Note** | Обробляється Spring без спеціального коду. `OrderRequest` не має анотацій `@Valid` / `@NotNull` / `@Min` |

### E3. Negative quantity

| | |
|---|---|
| **Condition** | `request.quantity()` дорівнює 0 або від'ємне |
| **Result** | Проходить валідацію (поріг `maxQuantity=50`), створюється замовлення з `quantity=0` або `quantity=-N` |
| | **⚠️ Логічна проблема — немає перевірки `quantity > 0`** |

---

## Configs

### orders.max-quantity

| | |
|---|---|
| **File** | [`application.yml`](../src/main/resources/application.yml), line 6 |
| **Value** | `50` |
| **Injection** | [`OrderService.java`](../src/main/java/com/acme/orders/OrderService.java), line 14 — `@Value("${orders.max-quantity:50}")` |
| **Fallback** | `50` (якщо ключ відсутній в YAML) |
| **Effect** | Визначає поріг спрацювання гілки помилки E1 |

### server.port

| | |
|---|---|
| **File** | [`application.yml`](../src/main/resources/application.yml), line 2 |
| **Value** | `8080` |
| **Effect** | Технічний — на логіку оформлення не впливає |

### orders.metrics.rebuild-delay-ms

| | |
|---|---|
| **File** | [`application.yml`](../src/main/resources/application.yml), line 8 |
| **Value** | `600000` |
| **Effect** | **Не використовується** в жодному класі проєкту |

---

## Tests

**File:** [`OrderControllerTest.java`](../src/test/java/com/acme/orders/OrderControllerTest.java), 1 тестовий метод.

```java
void placeOrderReturnsConfirmedStatus() {
    // given — hardcoded maxQuantity=50
    // when  — request(SKU-1, 2, tok-test)
    // then  — assert result.status() == "CONFIRMED"
}
```

**Що тестується:** лише happy path — коректні дані, quantity=2.

**Що не тестується:**
- Quantity > maxQuantity (error path E1)
- Quantity = 0 або від'ємне (error path E3)
- Відсутність SKU
- Відсутність paymentToken
- Порожнє тіло запиту

---

## Unverified

- **`paymentToken`** — приймається в `OrderRequest`, але ніде не обробляється. Оплата не виконується, токен не валідується.
- **ID замовлення** — `OrderRepository` генерує ID, але `OrderEntity` (record) не має поля для нього. Клієнт не отримує ID створеного замовлення.
- **Статус `"CONFIRMED"`** — жорстко закодований в `OrderService.java:26`. Немає інших статусів або логіки переходу статусів.
- **`docs/RUNTIME_FLOW.md`** — цей файл раніше був порожнім (0 байт), документація була відсутня.