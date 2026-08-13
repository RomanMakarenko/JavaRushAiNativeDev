package com.acme.commerce.refunds;

// Запит на повернення. idempotencyKey використовується для захисту від дублікатів під час retry.
public record RefundRequest(String idempotencyKey, long amount) {
}