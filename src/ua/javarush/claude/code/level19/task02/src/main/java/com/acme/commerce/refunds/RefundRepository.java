package com.acme.commerce.refunds;

// Сховище записів про повернення (ledger).
public class RefundRepository {

    public RefundResult save(RefundRequest request) {
        // Зберігає запис про повернення. Захист від дублювання за idempotency key поки що відсутній.
        return new RefundResult(request.idempotencyKey(), request.amount(), "CREATED");
    }
}