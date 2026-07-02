package com.example.shop.checkout;

/**
 * Запит на оформлення замовлення.
 */
public record CheckoutRequest(String customerEmail, String productId, int quantity) {
}