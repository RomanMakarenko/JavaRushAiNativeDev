# Layer 1 Review — CheckoutService Discount Feature

## Scope Summary

**File reviewed:** `commerce-os/src/main/java/com/example/store/CheckoutService.java`

**Commit:** `f1de31f` (l16 t08)

**Diff:** The `calculateTotal` method was changed from a 1-argument signature (taking only `lineItems`) to a 2-argument signature (adding `discountRate`). A discount calculation was added inline: `discount = subtotal × discountRate`, then `total = subtotal − discount`.

| Before | After |
|--------|-------|
| `calculateTotal(List<BigDecimal>)` | `calculateTotal(List<BigDecimal>, BigDecimal)` |
| Returns subtotal with `setScale(2, HALF_UP)` | Applies discount before returning |

## Findings

### F1 — Null `discountRate` causes NullPointerException

**File:** `CheckoutService.java:20`
**Severity:** HIGH — runtime crash, no recovery

`BigDecimal.multiply(null)` throws `NullPointerException`. If a caller passes `null` as `discountRate` (e.g., from an unset `Optional<BigDecimal>` or missing request field), the checkout crashes with no diagnostic.

**Failure scenario:** Any request where `discountRate` resolves to `null` → unhandled NPE → HTTP 500 / checkout abort.

---

### F2 — Negative `discountRate` inverts discount, overcharging the customer

**File:** `CheckoutService.java:20`
**Severity:** HIGH — financial logic error

No precondition validates that `discountRate >= 0`. A negative value produces `discount = subtotal × (-0.10) = negative` → `total = subtotal − (negative) = subtotal + |discount|`, inflating the final price.

**Failure scenario:** `calculateTotal(items, -0.1)` → customer is charged *more* than the full price instead of receiving a discount.

---

### F3 — `discountRate > 1.0` produces a negative total

**File:** `CheckoutService.java:21`
**Severity:** HIGH — financial logic error

No precondition validates that `discountRate <= 1.0`. A value > 1.0 means `discount > subtotal` → `total` becomes negative.

**Failure scenario:** `calculateTotal(items, 1.5)` → `total = subtotal − 1.5×subtotal = −0.5×subtotal`, producing a negative charge.

---

### F4 — Backward-incompatible API change (no overload preserved)

**File:** `CheckoutService.java:13`
**Severity:** MEDIUM — breaks existing callers

The 1-argument overload was removed entirely. Existing callers of `calculateTotal(List<BigDecimal>)` fail to compile. No backward-compatible shim or default-discount overload was provided.

**Failure scenario:** Any client invoking `calculateTotal(items)` → compilation error after the change.

---

### F5 — Null `lineItems` list causes NullPointerException

**File:** `CheckoutService.java:15`
**Severity:** MEDIUM — pre-existing gap in touched code

The enhanced `for` loop unconditionally dereferences `lineItems`. Passing `null` (e.g., from a DAO that returns null on empty results) triggers NPE before any business logic runs.

**Failure scenario:** `calculateTotal(null, rate)` → NPE at iteration start.

---

### F6 — Null element in `lineItems` causes NullPointerException

**File:** `CheckoutService.java:16`
**Severity:** MEDIUM — pre-existing gap in touched code

`subtotal.add(null)` throws NPE. A single null entry in the list (data corruption, partial load) crashes the entire checkout.

**Failure scenario:** `lineItems = [10, null, 20]` → NPE on the second add, order lost.

---

### F7 — No Javadoc documenting the `discountRate` contract

**File:** `CheckoutService.java:13`
**Severity:** LOW — mis-use surface

`discountRate` is a decimal fraction (0.10 = 10%), not a percentage (10). Without documentation, a developer could pass `10` (meaning 10%), which is interpreted as 1000%, producing a large negative total.

**Failure scenario:** `calculateTotal(items, 10)` → `discount = subtotal × 10` → huge negative total, no indication of the mistake.

## Recommended Local Decision

**Decision: `needs changes`**

Rationale: F1–F4 are blocking defects (NPE on null input, unvalidated discount range, backward-incompatible API change) that must be resolved before the change is safe to merge. F5–F6 are pre-existing null-safety gaps in touched code that are recommended to fix alongside the new changes. F7 is a documentation improvement.