package com.example.commerce.orders;

// Одна позиція кошика.
public record OrderItem(String sku, int quantity) {
}