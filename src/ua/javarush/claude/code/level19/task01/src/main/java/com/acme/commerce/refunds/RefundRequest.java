package com.acme.commerce.refunds;

// Запит на повернення. idempotencyKey використовується для захисту від дублів при retry.
public record RefundRequest(String idempotencyKey, long amount) {
}
