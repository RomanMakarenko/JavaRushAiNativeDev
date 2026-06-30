package com.acme.orders;

// Вхідні дані запиту на оформлення замовлення.
public record OrderRequest(String sku, int quantity, String paymentToken) {
}