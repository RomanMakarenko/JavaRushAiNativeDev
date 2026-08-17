package com.example.orders;

import java.math.BigDecimal;

/** Вихідний DTO замовлення. Повертається з REST-ендпоінтів, змінювати форму не можна. */
public record OrderResponse(String orderId, String status, BigDecimal amount) {
}