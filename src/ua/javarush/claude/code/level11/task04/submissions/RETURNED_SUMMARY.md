# Резюме: `GET /api/orders`

> Ізольований аналіз ендпоінта в межах пакета `com.example.store.orders`

---

## Структура (7 файлів)

| Файл | Шлях (відносно пакета) | Роль |
|------|------------------------|------|
| `Order.java` | `main/.../orders/Order.java` | Доменна модель (record) |
| `OrderResponse.java` | `main/.../orders/OrderResponse.java` | DTO відповіді (record) |
| `OrderRepository.java` | `main/.../orders/OrderRepository.java` | Інтерфейс доступу до даних |
| `OrderService.java` | `main/.../orders/OrderService.java` | Бізнес-логіка (@Service) |
| `OrderController.java` | `main/.../orders/OrderController.java` | REST-контролер (@RestController) |
| `OrderServiceTest.java` | `test/.../orders/OrderServiceTest.java` | JUnit 5 тест сервісу |
| `RETURNED_SUMMARY.md` | `submissions/RETURNED_SUMMARY.md` | Цей файл |

---

## Сигнатура ендпоінта

```
GET /api/orders?customerId={customerId}
```

---

## Request-Response Flow

### 1. HTTP-запит → `OrderController.java:24`

```java
@GetMapping("/api/orders")
public List<OrderResponse> listOrders(@RequestParam String customerId)
```

- **`@RestController`** — кожне повернення автоматично серіалізується в JSON.
- **`@RequestParam`** — `customerId` обов'язковий (ні `required=false`, ні `defaultValue`). Відсутність → 400 Bad Request від Spring.
- Немає обробки CORS, безпеки, контент-негойіації.

### 2. Controller → `OrderService.java:22` (бізнес-логіка)

```java
public List<Order> findByCustomer(String customerId) {
    return orderRepository.findByCustomerId(customerId).stream()
            .sorted(Comparator.comparing(Order::createdAt))
            .toList();
}
```

- Викликає репозиторій, отримує всі замовлення клієнта.
- Сортує **за зростанням `createdAt`** (найстаріші першими).

### 3. Service → `OrderRepository.java:12` (інтерфейс)

```java
List<Order> findByCustomerId(String customerId);
```

- **Конкретної реалізації в цьому пакеті немає.** Реалізація має бути надана ззовні через Spring-контекст (наприклад, інший модуль або @Configuration).
- Це чистий Java-інтерфейс, не Spring Data — жодних `@Repository`, `extends JpaRepository`, автоматичних SQL-генерацій.

### 4. Повернення даних: `Order.java:9` → `OrderResponse.java:10` (маппінг DTO)

- **`Order`** (`id`, `customerId`, `total`, `createdAt`) — доменний record без JPA.
- **`OrderResponse`** (`id`, `total`, `createdAt`) — публічне DTO, **`customerId` свідомо виключено** (клієнт уже знає, кого запитував).
- Маппінг через статичний `OrderResponse.from(Order)`.

### 5. JSON-відповідь

```json
[
  {"id": "o1", "total": 10.00, "createdAt": "2026-01-01T10:00:00Z"},
  {"id": "o2", "total": 20.00, "createdAt": "2026-01-02T10:00:00Z"}
]
```

- Пустий результат → `[]` (не 404).

---

## Покриття тестами

`OrderServiceTest.java:18` — один тест:

- **`sortsByCreatedAtAscending()`** — передає репозиторій, який повертає замовлення в зворотному хронологічному порядку, і перевіряє, що сервіс сортує їх за зростанням.

**Чого бракує:**
- Тесту контролера (`@WebMvcTest`) — інтеграція з Spring MVC не перевірена.
- Тесту на порожній результат.
- Тесту на null / порожній customerId.
- Тесту на відмову репозиторія (кидок винятку).
- Тесту, що `OrderResponse` коректно мапить поля.

---

## Ключові спостереження

| Аспект | Статус |
|--------|--------|
| Архітектура | Чиста тришарова: Controller → Service → Repository |
| DI | Конструкторна ін'єкція скрізь — добре |
| Сортування | In-memory в сервісі (не в БД) — проблема на великих даних |
| Реалізація репозиторія | Відсутня в пакеті — залежність від зовнішнього Spring-контексту |
| Валідація параметрів | Лише `@RequestParam` — без `@Valid`, без власних перевірок |
| Обробка помилок | Відсутня — покладається на стандартну Spring помилку 400 |
| customerId у відповіді | Свідомо виключено — вдалий дизайн DTO |
| Безпека | Немає — аутентифікація/авторизація не налаштовані |
| Тестування контролера | Відсутнє |
| Граничні випадки | Не покриті |

---

## Open Questions

1. **Де реалізація `OrderRepository`?** Без неї application context не стартує. Чи є вона в іншому модулі, чи це заглушка / in-memory імплементація?
2. **Чому сортування in-memory, а не на рівні БД?** Припустимо для малих даних, але для реального продакшну `ORDER BY created_at` у запиті значно ефективніший.
3. **Чи потрібна пагінація?** Клієнт може мати тисячі замовлень — повернення всього списку без `page`/`size` ризиковано.
4. **Чи є обробка винятків на рівні контролера?** `@ExceptionHandler` або `@ControllerAdvice` не знайдено; необроблені помилки репозиторія підуть у 500 Internal Server Error.
5. **Як тестувати ендпоінт інтеграційно?** Відсутній `@WebMvcTest(OrderController.class)` — Spring-контекст контролера не перевіряється.
6. **Чи будуть інші методи в `OrderRepository`?** Зараз лише `findByCustomerId`. Розширення (findById, save, delete) не передбачені контрактом.
7. **Чи має бути `@RequestMapping("/api")` на рівні класу?** Зараз `/api/orders` захардкоджено в `@GetMapping` — винесення `/api` на клас дало б гнучкість для інших ендпоінтів (`/api/products`, `/api/cart`).