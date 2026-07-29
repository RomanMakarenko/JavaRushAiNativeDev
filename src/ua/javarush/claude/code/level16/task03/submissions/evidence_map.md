# Evidence Map — `GET /api/coupons`

> Створено після інспекції Explore-агента.
> Робоча директорія: `.../level16/task03`

---

## 1. Project Layout

```
task03/
├── commerce-os/
│   └── src/
│       ├── main/java/com/example/store/coupons/
│       │   ├── CouponController.java       ← REST-вхід
│       │   ├── CouponService.java          ← бізнес-логіка
│       │   ├── CouponRepository.java       ← інтерфейс доступу (без impl)
│       │   ├── Coupon.java                 ← доменна сутність
│       │   ├── CouponView.java             ← DTO відповіді
│       │   ├── CurrencyConverter.java      ← конвертація валют
│       │   └── RateProvider.java           ← інтерфейс курсу валют (без impl)
│       └── test/java/com/example/store/coupons/
│           └── CouponServiceTest.java      ← unit-тести CouponService
└── submissions/
    ├── HANDOFF_TRACE.md
    ├── evidence_map.md                     ← цей файл
    └── plan.md
```

---

## 2. Flow Diagram (Request → Response)

```
Client
  │  GET /api/coupons?currency=EUR
  ▼
CouponController.listCoupons(currency)          ← @RestController, @RequestMapping("/api/coupons")
  │
  ▼
CouponService.findActiveCoupons(currency)       ← @Service
  │
  ├──► CouponRepository.findActive()            ← List<Coupon>
  │
  │   (для кожного Coupon)
  │   toView(coupon, currency)
  │     ├── currency == null || currency == coupon.baseCurrency
  │     │   └── discountAmount as-is
  │     └── currency != coupon.baseCurrency
  │         └── CurrencyConverter.convert(amount, from, to)
  │               ├──► RateProvider.rate(from, to)     ← BigDecimal
  │               └── amount * rate, HALF_EVEN, 2 decimals
  │
  ▼
List<CouponView> → JSON
```

---

## 3. Component Cards

### 3.1 CouponController.java — REST Controller

| Атрибут | Значення |
|---|---|
| Анотація | `@RestController`, `@RequestMapping("/api/coupons")` |
| Метод | `listCoupons(@RequestParam(required = false) String currency)` |
| HTTP | `GET` |
| Повертає | `List<CouponView>` (JSON) |
| Залежності | `CouponService` (інжектований через конструктор) |

**Код:**
```java
@GetMapping
public List<CouponView> listCoupons(@RequestParam(required = false) String currency) {
    return couponService.findActiveCoupons(currency);
}
```

Параметр `currency` опціональний, передається "як є" в сервіс.

---

### 3.2 CouponService.java — Business Logic

| Атрибут | Значення |
|---|---|
| Анотація | `@Service` |
| Залежності | `CouponRepository`, `CurrencyConverter` |

**Метод `findActiveCoupons(String currency)`:**
1. `couponRepository.findActive()` — отримує всі активні купони.
2. Для кожного `Coupon` викликає приватний `toView()`.
3. `toView`: якщо `currency != null && !currency.equals(coupon.baseCurrency)` → конвертує через `CurrencyConverter.convert()`; інакше повертає суму без змін.

---

### 3.3 CouponRepository.java — Data Access Interface

| Атрибут | Значення |
|---|---|
| Тип | Plain Java interface (не Spring Data) |
| Метод | `List<Coupon> findActive()` |
| Імплементація | Відсутня в робочій директорії (у тесті — lambda) |

---

### 3.4 Coupon.java — Domain Entity

| Поле | Тип |
|---|---|
| `code` | `String` |
| `discountAmount` | `BigDecimal` |
| `baseCurrency` | `String` |

Не містить JPA-аннотацій — це POJO, не entity в сенсі ORM.

---

### 3.5 CouponView.java — Response DTO

| Поле | Тип |
|---|---|
| `code` | `String` |
| `discountAmount` | `BigDecimal` |
| `currency` | `String` |

Відрізняється від `Coupon` тим, що `currency` — це *результуюча* валюта (або base, або конвертована).

---

### 3.6 CurrencyConverter.java — Currency Conversion

| Атрибут | Значення |
|---|---|
| Анотація | `@Component` |
| Залежності | `RateProvider` |
| Метод | `convert(BigDecimal amount, String from, String to)` |

**Алгоритм:**
1. `rateProvider.rate(from, to)`
2. `amount.multiply(rate)`
3. Округлення: `RoundingMode.HALF_EVEN`, 2 знаки після коми.

---

### 3.7 RateProvider.java — Exchange Rate Interface

| Атрибут | Значення |
|---|---|
| Тип | Plain Java interface |
| Метод | `BigDecimal rate(String from, String to)` |
| Імплементація | Відсутня в робочій директорії (у тесті — lambda) |

---

## 4. Findings / Спостереження

### 🔴 Finding 1: Тест на конвертацію може фейлитись через невідповідність округлення

**Файли:** `CurrencyConverter.java:12` → `CouponServiceTest.java:35`

- Тест `convertsAmountForForeignCurrency()`:
  - Вхід: `10.005 USD → EUR`, rate = `0.90`, результат = `10.005 × 0.90 = 9.0045`
  - Очікування в тесті: `9.01` (що відповідає `HALF_UP`)
  - Фактичне округлення в конвертері: `HALF_EVEN`
  - `HALF_EVEN` на `9.0045` → **`9.00`** (парний сусід)
  - Тест очікує `9.01`, конвертер поверне `9.00` → **AssertionError**

**Статус:** Потенційний баг — тест не пройде, якщо запустити його з поточним `CurrencyConverter`.

---

### 🟡 Finding 2: Немає імплементацій репозиторія та RateProvider у робочій директорії

- `CouponRepository` — лише інтерфейс; `findActive()` нічим не підкріплений.
- `RateProvider` — лише інтерфейс; реальний курс валют не налаштовано.
- Це може бути нормально для фрагмента проєкту, але повноцінний запуск неможливий без контексту (Spring Boot app, інші модулі).

---

### 🟢 Finding 3: Код добре розділений на шари

- Controller → Service → Repository (звичайна тришарова архітектура).
- `CurrencyConverter` винесено в окремий компонент — хороша SRP.
- `CouponView` як окремий DTO, не засмічує доменну сутність.

---

## 5. Dependency Graph

```
CouponController
  └── CouponService
        ├── CouponRepository (interface → List<Coupon>)
        └── CurrencyConverter
              └── RateProvider (interface → BigDecimal rate)
```

Всі зв'язки — через конструкторну ін'єкцію (`@RequiredArgsConstructor` або явний конструктор).

---

## 6. Test Coverage

| Тест | Що перевіряє | Статус |
|---|---|---|
| `returnsBaseCurrencyAmountWhenCurrencyMatches()` | `currency=USD`, `coupon.baseCurrency=USD` → сума без змін | ✅ |
| `convertsAmountForForeignCurrency()` | `currency=EUR`, `coupon.baseCurrency=USD`, rate 0.90 → конвертація | ⚠️ Див. Finding 1 |