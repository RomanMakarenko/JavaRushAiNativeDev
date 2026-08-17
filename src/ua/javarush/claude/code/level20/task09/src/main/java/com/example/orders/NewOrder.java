package com.example.orders;

import java.math.BigDecimal;

/**
 * Вхідні дані для створення замовлення (частина public API сервісу).
 */
public record NewOrder(String customerId, BigDecimal amount) {
}