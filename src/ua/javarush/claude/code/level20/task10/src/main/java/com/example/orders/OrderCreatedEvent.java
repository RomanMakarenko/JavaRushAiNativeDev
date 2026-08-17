package com.example.orders;

import java.math.BigDecimal;

/**
 * Контракт події про створення замовлення.
 * Payload змінювати не можна — це зовнішній event contract.
 */
public record OrderCreatedEvent(long orderId, String customerId, BigDecimal amount) {
}