package com.example.shop.refund;

import java.math.BigDecimal;
import java.time.Instant;

/** Запит на повернення коштів в inbox оператора. */
public class RefundRequest {

    private final String id;
    private final String orderId;
    private final BigDecimal amount;
    private final Instant createdAt;

    public RefundRequest(String id, String orderId, BigDecimal amount, Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}