# Review: `inputs/change.patch`

**File:** `src/main/java/com/example/store/refund/RefundInboxService.java`

---

## Summary

The patch makes two changes to `RefundInboxService`: (1) re-sorts the agent inbox by refund amount descending instead of creation date ascending, and (2) adds a new `markProcessed` method to update request status. Both changes contain correctness and robustness issues that should be addressed before merging.

---

## Findings

### Blocker

**1. NullPointerException risk when sorting by `getAmount()`**
- `file:22` — `Comparator.comparing(RefundRequest::getAmount).reversed()`
- `RefundRequest.getAmount()` returns `BigDecimal` (an object type). If any request in the inbox has a null `amount`, `Comparator.comparing()` will throw a `NullPointerException` when the stream is evaluated. The previous sort by `getCreatedAt()` had the same theoretical risk, but introducing a new sort key is the right moment to fix it. Either add `Comparator.nullsLast()` / `nullsFirst()` wrapping, or ensure the domain guarantees non-null `amount` via a precondition check before sorting.

**2. Unsafe string literal for status value**
- `file:27` — `repository.updateStatus(requestId, "PROCESSED");`
- The status `"PROCESSED"` is a bare magic string, not a constant, enum, or type-safe value. A typo (e.g., `"PROCESED"`, `"PROCESSED "` with trailing space) would silently persist an incorrect status in the database, and the string is inconsistent with any existing status constants the codebase might define. Introduce an enum (e.g., `RefundStatus.PROCESSED`) or at least a private static final constant so the value is defined once and reused.

### Major

**3. Silent idempotency / missing validation on `markProcessed`**
- `file:26-28` — `markProcessed(String requestId)` accepts the parameter without any validation. If `requestId` is `null`, `blank`, or refers to a non-existent entity, the repository call either fails with a cryptic error or silently succeeds without modifying any row. The method should validate its input (e.g., `Objects.requireNonNull(requestId)` or a `findById` check) and return a meaningful result (e.g., `boolean` indicating whether a row was actually updated).

**4. Sorting change alters implicit business contract without documentation**
- The original sort was by `createdAt` ascending (oldest first — FIFO queue). The new sort is by `amount` descending (largest first).
- `file:19-20` — No corresponding change in the method Javadoc (or `loadInbox` has no Javadoc at all). The new ordering prioritises high-value refunds over older, smaller ones. This changes the SLA experienced by customers with small refunds (they may never be processed if large refunds keep arriving) and should be verified with product/business stakeholders. The class-level comment `/** Сортування refund-запитів в inbox оператора. */` is also now stale if the sort changed.

### Minor

**5. No transaction demarcation on `markProcessed`**
- `file:26-28` — The method calls `repository.updateStatus(...)` outside any explicit transaction. If the repository relies on a connection-per-call model or if future changes add multiple repository calls to this method, partial updates could occur. Consider adding `@Transactional` (or equivalent) to make the data change atomic.

**6. `loadInbox` returns mutable list to the caller**
- `file:21-24` — `.collect(Collectors.toList())` returns a mutable `ArrayList` (on standard JDK implementations). The caller could add/remove elements, corrupting the intended ordering or leaking internal state. Use `.collect(Collectors.toUnmodifiableList())` or wrap with `Collections.unmodifiableList(...)` instead.

**7. Missing tests for the new public method**
- `file:26-28` — A new public method `markProcessed` is added with no test coverage visible in the diff. Since it mutates repository state, it should have at minimum a unit test verifying the correct status is passed and a test covering the null/empty `requestId` edge case.

---

## Open questions

1. **Package mismatch**: The patch targets `com.example.store.refund.RefundInboxService`, but the file that exists in the repository is at `com.example.shop.refund.RefundInboxService`. Are these the same module after a refactor, or is the patch against a different branch/task entirely?

2. **`repository` field and `loadInbox` method**: The patch assumes a `repository` dependency and a `findOpenByAgent` method that are not visible in the current source. Is `repository` injected via constructor/field and `updateStatus` defined on it? Confirming this would clarify whether `markProcessed` actually compiles.

3. **Intended status lifecycle**: What is the complete list of valid statuses in the system? If the current codebase uses an enum or constant class for refund statuses, `markProcessed` should reuse it instead of introducing a new magic string. Is there a `RefundStatus` enum elsewhere in the project?

4. **Concurrent access**: Could multiple operators call `loadInbox` and `markProcessed` on overlapping sets of refunds? If so, the new priority-based sorting might cause races (operator A starts processing a large refund, operator B sees it re-sorted to the top and also starts processing it). Are there any optimistic locking or deduplication guards in the repository layer?