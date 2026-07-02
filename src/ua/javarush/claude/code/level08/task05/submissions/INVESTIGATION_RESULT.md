# Investigation Result: Payment Touchpoints in `payments` & `refunds` modules

## Summary

The codebase contains exactly 5 production classes across two packages (`payments`, `refunds`)
that mediate **3 external-system touchpoints** (Stripe API × 2, message broker × 1).
There is a **confirmed semantic mismatch** at the module boundary: `orderId` is passed
where `chargeId` is expected (`RefundService:26 → StripeClient:22`). No error handling,
idempotency, logging, or input validation exists anywhere. Only 2 tests cover the happy
path with bare `assertNotNull`, and the `payments` package has zero tests.

---

## Evidence

### Confirmed touchpoints (file:line)

| # | Touchpoint | Module | File:Line | Nature |
|---|---|---|---|---|
| 1 | Charge creation → Stripe | `payments` → external | `PaymentService.java:17` → `StripeClient.java:16` | Direct delegation, no transformation |
| 2 | Refund → Stripe | `refunds` → external | `RefundService.java:26` → `StripeClient.java:22` | Direct call, **passes `orderId` as `chargeId`** |
| 3 | Refund event → broker | `refunds` → external | `RefundService.java:27` → `RefundEventPublisher.java:16` | Post-refund async event; body is empty stub |

### Confirmed internal entry points (file:line)

| # | Entry | Module | File:Line |
|---|---|---|---|
| 4 | Client-facing refund request | `refunds` → internal | `RefundController.java:17` → `RefundService.java:25` |
| 5 | `PaymentService` constructor injection | `payments` | `PaymentService.java:11-13` |
| 6 | `RefundService` constructor injection | `refunds` | `RefundService.java:19-22` |

### Confirmed gaps

- **Zero error handling** in all 5 production classes — no `try/catch`, no custom exceptions,
  no input validation.
- **Zero idempotency** — no idempotency keys for Stripe calls, no deduplication for events.
- **Zero logging** — no `Logger`, no `System.err`, no audit trail.
- **Zero tests in `payments` package** — no `PaymentServiceTest`, no `payments/` test directory.
- **Only 2 tests** (`RefundFlowTest`, `RefundServiceTest`), both using only `assertNotNull`.
- **No model/DTO classes** — everything flows as bare primitives (`String`, `long`).

---

## Assumptions

1. **The `orderId` → `chargeId` mismatch is a bug, not design.** `StripeClient:22` names its
   parameter `chargeId` — the refund module passes an order-scoped identifier where a
   charge-scoped one is expected. No mapping layer exists to resolve `orderId → chargeId`.
   *(Hypothesis: a `chargeId` lookup from `orderId` is missing, or the parameter name in
   `RefundService:26` should be renamed to `chargeId`.)*

2. **Stripe is the sole payment provider.** No abstraction layer, no alternative provider.

3. **`RefundEventPublisher` targets a message broker** (queue terminology suggests RabbitMQ /
   Kafka / SQS). Consumer code is absent from this codebase.

4. **Stubs are always-successful and deterministic** — `StripeClient` returns hardcoded
   strings (`"ch_demo"`, `"re_demo"`), `RefundEventPublisher.publish()` is an empty body.

5. **This is an educational/teaching codebase** — Javadocs repeatedly note "omitted for
   educational example."

6. **No DI framework intended** — manual constructor wiring only, no annotations or
   configuration classes.

---

## Open questions

1. **`orderId` vs `chargeId`:** Is the mismatch in `RefundService:26` a bug, or is there
   an implicit assumption that `orderId == chargeId`? If a bug, what is the expected
   `orderId → chargeId` resolution mechanism?

2. **Charge ID orphan:** `PaymentService.charge()` returns a `chargeId` (line 17) that is
   never stored or passed anywhere. Is a persistence layer or data flow between modules
   missing?

3. **Failure semantics:** What should happen when `StripeClient.createRefund()` fails or
   `RefundEventPublisher.publish()` throws? Should the refund be retried? Should the caller
   receive an exception or a fallback value?

4. **Event consumers:** What processes consume events published to the broker queue?
   (Not present in this codebase.)

5. **Missing `PaymentController`:** The `payments` module has no entry-point class
   equivalent to `RefundController`. Is this intentional or incomplete?

---

## Next steps

1. **Resolve `orderId`/`chargeId` mismatch** — decide on rename vs. lookup layer.
2. **Add error handling** — domain exceptions, `try/catch` around Stripe and event calls,
   idempotency keys for Stripe, compensation strategy for partial failures.
3. **Add input validation** — null/empty guards, positive-amount checks.
4. **Add logging** at every external call point (SLF4J or `java.util.logging`).
5. **Expand test coverage** — `PaymentServiceTest`, `StripeClientTest`,
   `RefundEventPublisherTest`, failure-mode tests, `publish()` invocation verification.
6. **Introduce shared model classes** (`Charge`, `Refund`, `PaymentEvent`) to strengthen
   module-boundary semantics.