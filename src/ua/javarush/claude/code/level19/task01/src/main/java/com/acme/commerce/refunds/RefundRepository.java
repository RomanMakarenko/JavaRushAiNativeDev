package com.acme.commerce.refunds;

// Сховище записів про повернення (ledger).
public class RefundRepository {

    public RefundResult save(RefundRequest request) {
        // Зберігає запис про повернення. Захист від дубля за idempotency key поки відсутній.
        return new RefundResult(request.idempotencyKey(), request.amount(), "CREATED");
    }
}
