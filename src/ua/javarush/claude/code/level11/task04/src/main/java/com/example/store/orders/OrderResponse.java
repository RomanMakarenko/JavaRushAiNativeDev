package com.example.store.orders;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO відповіді для GET /api/orders.
 * Формується з доменної моделі Order.
 */
public record OrderResponse(
        String id,
        BigDecimal total,
        Instant createdAt
) {
    // Перетворює доменний Order на публічну відповідь
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.id(), order.total(), order.createdAt());
    }
}