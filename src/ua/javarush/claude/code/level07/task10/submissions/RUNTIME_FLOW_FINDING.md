# RUNTIME_FLOW_FINDING — аналіз call chain `placeOrder`

## Поточний ланцюг (actual)

```
placeOrder(OrderRequest)
  │
  ├─ 1. paymentGateway.authorize(paymentToken, quantity)
  │     → PaymentGateway.java:9
  │     → Side effect: авторизація списання коштів у зовнішньому платіжному шлюзі
  │     → Повертає рядок "auth-{paymentToken}" (ігнорується caller-ом)
  │
  ├─ 2. inventoryService.reserve(sku, quantity)
  │     → InventoryService.java:9
  │     → Side effect: позначення товару як зарезервованого на складі
  │     → Може викинути InventoryUnavailableException (якщо товару недостатньо)
  │
  ├─ 3. orderRepository.save(new OrderEntity(sku, quantity, "CONFIRMED"))
  │     → OrderRepository.java:14
  │     → Side effect: AtomicLong.incrementAndGet() + store.put(id, entity)
  │     → Зберігає OrderEntity у ConcurrentHashMap<Long, OrderEntity>
  │
  └─ 4. return new OrderResult("CONFIRMED")
```

## Цільовий ланцюг (expected)

```
placeOrder(OrderRequest)
  │
  ├─ 1. inventoryService.reserve(sku, quantity)
  │     → Резервування товару на складі ПЕРЕД списанням коштів
  │     → Якщо товару немає — викинути виняток, не чіпаючи платіж
  │
  ├─ 2. paymentGateway.authorize(paymentToken, quantity)
  │     → Авторизація платежу тільки після підтвердження наявності товару
  │     → Результат authorize має бути збережено в OrderEntity
  │
  ├─ 3. orderRepository.save(new OrderEntity(sku, quantity, authId, "CONFIRMED"))
  │     → Збереження замовлення з ідентифікатором авторизації платежу
  │
  └─ 4. return new OrderResult("CONFIRMED")
```

## Розбіжність

| Аспект | Поточний код | Цільовий код |
|---|---|---|
| Порядок | authorize → reserve → save | reserve → authorize → save |
| Результат authorize | Ігнорується | Має зберігатися в OrderEntity |

**Файл:** `OrderService.java:23-24` — рядки поміняні місцями.

## Зачеплені side effects

1. **PaymentGateway.authorize()** — списання/блокування коштів. В поточному коді виконується до
   перевірки наявності товару. Якщо `reserve()` далі кине `InventoryUnavailableException`,
   платіж уже буде авторизовано, а замовлення не буде створено.

2. **InventoryService.reserve()** — зменшення доступної кількості товару на складі. Має бути
   виконано ПЕРЕД авторизацією платежу, щоб гарантувати, що товар реально доступний.

3. **OrderRepository.save()** — запис даних замовлення в сховище. В поточному коді виконується
   останнім, що правильно, але статус жорстко закодовано як `"CONFIRMED"`, і він не містить
   інформації про авторизацію платежу.

4. **Транзакційна цілісність відсутня:** всі три side effects виконуються незалежно — якщо
   `save()` падає після успішних `authorize` та `reserve`, система залишається в неузгодженому
   стані (кошти авторизовано, товар зарезервовано, замовлення не збережено).

## Висновок

Потрібно поміняти порядок викликів authorize та reserve, а також передати результат authorize
у OrderEntity. Рекомендований порядок: reserve → authorize → save.