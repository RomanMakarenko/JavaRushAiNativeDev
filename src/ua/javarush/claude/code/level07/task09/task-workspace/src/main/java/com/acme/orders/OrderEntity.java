package com.acme.orders;

// Запис замовлення, що зберігається.
public record OrderEntity(String sku, int quantity, String status) {
}