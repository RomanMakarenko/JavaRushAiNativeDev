package com.example.orders;

import java.math.BigDecimal;

/**
 * Доменна сутність замовлення.
 */
public record Order(long id, String customerId, BigDecimal amount) {

    public Order(String customerId, BigDecimal amount) {
        this(0L, customerId, amount);
    }
}