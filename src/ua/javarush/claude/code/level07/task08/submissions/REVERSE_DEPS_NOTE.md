# Reverse Dependencies: `Money.java`

## Risky File

| File | Package |
|------|---------|
| `shared/Money.java` | `com.acme.shared` |

**Risk Level: HIGH** — shared value-object across 3 service modules; any change propagates silently to all consumers.

---

## Inbound References (real files)

| # | Consumer (file) | Import | Usage |
|---|-----------------|--------|-------|
| 1 | `/src/main/java/com/acme/orders/OrderService.java` | `import com.acme.shared.Money;` | `calculateTotal(List<Money>)` — sums line items |
| 2 | `/src/main/java/com/acme/payments/RefundService.java` | `import com.acme.shared.Money;` | `calculateRefund(Money, Money)` — refund minus fee |
| 3 | `/src/main/java/com/acme/reports/RevenueService.java` | `import com.acme.shared.Money;` | `totalRevenue(List<Money>)` — aggregates charges |

---

## Affected Modules

All references reside within a **single Gradle subproject**:

```
level07/task08/
├── build.gradle.kts          (Spring Boot 3.2.5, java, data-jpa, postgresql)
└── src/main/java/com/acme/
    ├── orders/      → OrderService.java
    ├── payments/    → RefundService.java
    ├── reports/     → RevenueService.java
    └── shared/      → Money.java       ← risky file
```

**Module:** `com.acme` (group), `level07/task08` (subproject path).

Other levels (`level01`–`level06`, sibling `level07/task*`): **no references found**.

---

## Risk Note

`Money` is a **canonical record** (`long amountCents, String currency`) with arithmetic methods `plus`/`minus` that throw on currency mismatch. Because it is a value object shared across three service packages (`orders`, `payments`, `reports`), any change to its constructor, fields, or arithmetic semantics has immediate reach:
