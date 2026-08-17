package com.example.orders;

import java.util.List;

/**
 * Сервіс оформлення замовлення: валідація позицій і розрахунок підсумкової суми.
 */
public class OrderService {

    private final DiscountCalculator discountCalculator;

    public OrderService(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    /**
     * Обчислює підсумок замовлення з урахуванням знижки. Перед розрахунком перевіряє позиції.
     */
    public long total(List<OrderItem> items) {
        // Inline-блок валідації: кандидат на винесення в private helper
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
        for (OrderItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("item must not be null");
            }
            if (item.quantity() <= 0) {
                throw new IllegalArgumentException("quantity must be positive");
            }
            if (item.unitPrice() < 0) {
                throw new IllegalArgumentException("unit price must not be negative");
            }
        }

        long subtotal = 0;
        for (OrderItem item : items) {
            subtotal += (long) item.quantity() * item.unitPrice();
        }
        long discount = discountCalculator.discountFor(subtotal);
        return subtotal - discount;
    }
}