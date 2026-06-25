package com.example.commerce.refund;

/**
 * Вхідний DTO запиту на рішення щодо повернення.
 * Частина публічного API refund-flow — у довгій задачі не змінюється.
 */
public record RefundRequest(String orderId, int amount) {
}