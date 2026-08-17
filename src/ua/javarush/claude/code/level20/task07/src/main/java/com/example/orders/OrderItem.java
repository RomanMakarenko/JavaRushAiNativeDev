package com.example.orders;

/**
 * Позиція замовлення: кількість і ціна за одиницю (у копійках).
 */
public record OrderItem(int quantity, long unitPrice) {
}