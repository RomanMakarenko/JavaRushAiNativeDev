package com.acme.commerce.refunds;

// Результат обробки повернення.
public record RefundResult(String idempotencyKey, long amount, String status) {
}
