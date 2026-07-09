package com.example.store.orders;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Доменна модель замовлення.
 */
public record Order(
        String id,
        String customerId,
        BigDecimal total,
        Instant createdAt
) {
}