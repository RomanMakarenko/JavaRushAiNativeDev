# HANDOFF TRACE

> Фіксація механізму research stage та переданого artifact-у.

---

## Research Stage

| Атрибут | Значення |
|---|---|
| **Механізм** | `Agent` (subagent type: `Explore`) — read-only investigation worker |
| **Завдання** | Дослідити код, пов'язаний з `GET /api/coupons`, у межах робочої директорії |
| **Промпт** | "Thoroughly investigate the code related to GET /api/coupons in this project… Search for: any files containing 'coupon'/'Coupon', '/api/coupons', controller/service/repository files, project structure, config/routing" |
| **Режим** | Read-only, без модифікацій файлів |
| **Охоплення** | 7 main-файлів, 1 тестовий файл, 3 порожніх файли в `submissions/` |

### Чому саме Explore

- Потрібен був broad fan-out search — виявити всі файли, які стосуються купонів, без наперед відомих назв чи шляхів.
- Explore читає ексцерпти, а не цілі файли — достатньо для локалізації коду, без ризику переповнити контекст головної сесії.
- Асинхронний запуск: research відбувався в background, дозволяючи продовжувати діалог.

---

## Artifact, переданий далі

```
evidence_map.md
```

### Що містить artifact

| Секція | Опис |
|---|---|
| **Project Layout** | Повна структура директорій (шляхи до кожного файлу) |
| **Flow Diagram** | Візуальна схема `Client → Controller → Service → Repository/Converter → Response` |
| **Component Cards** | 7 карток — по одній на кожен клас (анотації, методи, залежності, код) |
| **Findings** | 3 знахідки: 🔴 rounding mismatch, 🟡 missing impls, 🟢 хороша архітектура |
| **Dependency Graph** | CouponController → CouponService → CouponRepository + CurrencyConverter → RateProvider |
| **Test Coverage** | Таблиця 2 тестів зі статусом |

### Зв'язок: evidence_map → plan

**Evidence map** (descriptive — що є) → **Plan** (prescriptive — що робити):

| Елемент evidence_map | Перейшов у plan як |
|---|---|
| Finding 1 (rounding mismatch) | Task A — узгодити `HALF_EVEN` vs `HALF_UP` |
| Finding 2 (missing impls) | Task C — додати `InMemoryCouponRepository` та `FixedRateProvider` |
| Finding 3 (архітектура) | Зафіксовано як зелене спостереження, змін структури не потребує |
| Test Coverage §6 | Task B — доповнити покриття |
| Component Cards §3 + Flow §2 | Architecture Summary — каркас для розуміння flow |

---

## Підсумок

```
User prompt
    │
    ▼
Explore Agent (read-only research) ──► evidence_map.md (descriptive artifact)
                                              │
                                              ▼ (виключно на основі evidence_map)
                                        plan.md (prescriptive artifact — tasks)
                                              │
                                              ▼
                                        HANDOFF_TRACE.md (цей файл)
```