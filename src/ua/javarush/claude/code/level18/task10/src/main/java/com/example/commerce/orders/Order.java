package com.example.commerce.orders;

import java.util.List;

// Створене замовлення.
public record Order(String id, List<OrderItem> items) {
}