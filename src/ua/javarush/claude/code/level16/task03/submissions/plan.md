# Plan — `GET /api/coupons`

> Складено на основі `evidence_map.md` (артефакт дослідження коду).
> Не містить посилань на сирий код — тільки на висновки з артефакту.

---

## 1. Goal

Привести ендпоїнт `GET /api/coupons` до узгодженого стану: виправити невідповідність округлення в конвертації валют, доповнити тести та зафіксувати відсутні імплементації.

---

## 2. Architecture Summary (з evidence_map)

| Шар | Компонент | Залежності |
|---|---|---|
| **Controller** | `CouponController` | `CouponService` |
| **Service** | `CouponService` | `CouponRepository`, `CurrencyConverter` |
| **Data Access** | `CouponRepository` (*interface*) | — |
| **Domain** | `Coupon` | — |
| **DTO** | `CouponView` | — |
| **Converter** | `CurrencyConverter` | `RateProvider` |
| **Rate Source** | `RateProvider` (*interface*) | — |

**Flow (скорочено з evidence_map §2):**
1. HTTP `GET /api/coupons?currency=…` → Controller
2. Controller → `CouponService.findActiveCoupons(currency)`
3. Service → `CouponRepository.findActive()` → `List<Coupon>`
4. Для кожного `Coupon`: якщо валюта запиту відрізняється від baseCurrency — конвертація через `CurrencyConverter` з округленням `HALF_EVEN`
5. Відповідь: `List<CouponView>` як JSON

---

## 3. Problem Analysis (з evidence_map §4)

### Finding 1 (🔴) — Rounding mode mismatch

| Джерело | Значення |
|---|---|
| Округлення в `CurrencyConverter` | `HALF_EVEN` |
| Очікування в тесті `convertsAmountForForeignCurrency` | `9.01` (відповідає `HALF_UP`) |
| Конкретний кейс | `10.005 × 0.90 = 9.0045` → `HALF_EVEN` дає `9.00`, тест чекає `9.01` |

**Наслідок:** Тест впаде з `AssertionError` при запуску.

**Питання щодо вибору:**
- `HALF_EVEN` (банківське округлення) — статистично менша похибка, стандарт для фінансів (IEEE 754)
- `HALF_UP` (шкільне округлення) — інтуїтивніше, але вносить систематичне зміщення вгору

**Рішення має бути узгоджене** — або конвертер, або тест.

### Finding 2 (🟡) — Відсутні імплементації

| Інтерфейс | Метод | Імплементація в директорії |
|---|---|---|
| `CouponRepository` | `findActive()` | ❌ |
| `RateProvider` | `rate(from, to)` | ❌ |

У тестах використовуються lambda-імплементації, що нормально для unit-тестів. Для повноцінного запуску потрібні реальні імплементації (наприклад, in-memory список або HTTP-клієнт курсів).

### Finding 3 (🟢) — Архітектура

Код має чисту тришарову структуру, компоненти розділені за принципом SRP. Жодних змін структури не потребує.

---

## 4. Tasks

### Task A: Узгодити rounding mode

**На основі:** evidence_map §4 Finding 1, §3.6 (CurrencyConverter), §6 (Test Coverage)

**Варіант А1 (рекомендовано):** Змінити конвертер на `HALF_UP`
- Виправити `CurrencyConverter.convert()`: `RoundingMode.HALF_EVEN` → `RoundingMode.HALF_UP`
- Тест очікує `9.01` — конвертер почне повертати `9.01`
- ✅ Тести проходять, конвертація інтуїтивна

**Варіант А2:** Змінити тест на `HALF_EVEN`
- Виправити тест: очікуване значення з `9.01` на `9.00`
- Додати коментар, що `HALF_EVEN` — навмисний вибір (банківське округлення)
- ✅ Тести проходять, конвертер відповідає фінансовим стандартам

### Task B: Доповнити тестовий покритий (після фіксу Task A)

**На основі:** evidence_map §3.2, §3.6, §6

- Додати тест на `currency = null` (без конвертації)
- Додати тест на `currency = ""` (порожній рядок — якщо валідація не спрацьовує)
- Додати тест на `currency`, що збігається з `baseCurrency` (вже є, перевірити що проходить)
- Додати тест на конвертацію з однаковою валютою (`USD → USD`)
- Додати тест на множинні купони — перевірити, що всі конвертуються однаково
- Додати тест на `RateProvider`, що повертає `null` або кидає виняток (edge case)

### Task C: Додати імплементації для локального запуску (опціонально)

**На основі:** evidence_map §4 Finding 2, §3.3, §3.7

- **`InMemoryCouponRepository`**: імплементує `CouponRepository.findActive()`, повертає захардкоджений список купонів
- **`FixedRateProvider`**: імплементує `RateProvider.rate()`, повертає фіксовані курси для тестових валют (наприклад, `USD→EUR = 0.90`, `EUR→USD = 1.10`)
- Або **`StubRateProvider`** — заглушка для ручного тестування

---

## 5. Dependencies / Ordering

```
Task A ──розблоковує──► Task B

Task C (опціонально, незалежний від A і B)
```

- **Task A** — обов'язковий (фікс бага)
- **Task B** — після Task A (тести розширюються після фіксу)
- **Task C** — опціональний, можна паралельно (незалежний)

---

## 6. Risk Assessment

| Ризик | Ймовірність | Вплив | Мітігація |
|---|---|---|---|
| Вибраний rounding mode не узгоджений з командою | medium | низький | Винести рішення в Task A як вибір (A1 vs A2) |
| Після зміни конвертера попливуть інші тести (якщо є в інших модулях) | low | середній | Перевірити, що конвертер ніде більше не використовується (або виправити) |
| Відсутність реальних імплементацій блокує інтеграційне тестування | high | низький | Task C вирішує це, але не є критичним для unit-тестів |

---

## 7. Acceptance Criteria

1. Тест `convertsAmountForForeignCurrency` проходить (після вибору A1 або A2)
2. Тест `returnsBaseCurrencyAmountWhenCurrencyMatches` проходить (без змін)
3. Додані тести на `null`/порожній `currency`, однакову валюту, множинні купони (Task B)
4. (Опціонально) Реалізовано `InMemoryCouponRepository` та `FixedRateProvider` для локального запуску