package com.example.commerce.orders;

import java.util.List;

// Тіло запиту на створення замовлення: список товарів кошика.
public record OrderRequest(List<OrderItem> items) {
}
