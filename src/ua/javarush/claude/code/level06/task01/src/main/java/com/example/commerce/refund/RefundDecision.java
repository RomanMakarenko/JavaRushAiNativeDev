package com.example.commerce.refund;

/**
 * Вихідний DTO рішення щодо повернення.
 * Частина публічного API refund-flow — у довгому завданні не змінюється.
 */
public record RefundDecision(String orderId, boolean manualApprovalRequired) {
}