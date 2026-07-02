package com.example.shop.checkout;

/**
 * Відповідь з даними створеного замовлення.
 */
public record OrderResponse(String orderId, String customerEmail) {
}