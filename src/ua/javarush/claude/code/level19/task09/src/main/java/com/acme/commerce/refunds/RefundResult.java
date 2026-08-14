package com.acme.commerce.refunds;

// Результат обробки запиту на повернення коштів.
public record RefundResult(String refundId, String status, long amount) {
}