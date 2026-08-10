package com.example.commerce.orders;

import java.util.List;

/** Тіло запиту на оформлення замовлення: список позицій кошика. */
public record OrderRequest(List<OrderItem> items) {
}