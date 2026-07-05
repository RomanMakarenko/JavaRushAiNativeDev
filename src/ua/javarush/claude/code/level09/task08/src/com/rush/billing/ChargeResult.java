package com.rush.billing;

/** Результат списання. */
public record ChargeResult(String paymentId, String externalId) {
}