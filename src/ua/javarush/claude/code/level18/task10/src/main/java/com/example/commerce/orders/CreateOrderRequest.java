package com.example.commerce.orders;

import java.util.List;

// Запит на створення замовлення: список позицій кошика.
public record CreateOrderRequest(List<OrderItem> items) {
}