package com.example.commerce.refund;

/**
 * Вхідний DTO запиту на рішення щодо повернення.
 * Частина публічного API refund-flow — у довгому завданні не змінюється.
 */
public record RefundRequest(String orderId, int amount) {
}