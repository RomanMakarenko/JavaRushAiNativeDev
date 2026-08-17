package com.example.orders;

import java.math.BigDecimal;

/** Вхідний DTO запиту на створення замовлення. Частина публічного контракту controller-рівня. */
public record OrderRequest(String orderId, BigDecimal amount) {
}