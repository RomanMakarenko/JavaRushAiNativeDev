package com.example.commerce.orders;

/** Одна позиція кошика: ідентифікатор товару та кількість. */
public record OrderItem(String sku, int quantity) {
}