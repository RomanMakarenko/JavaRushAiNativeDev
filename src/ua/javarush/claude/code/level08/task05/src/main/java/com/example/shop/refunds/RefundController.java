package com.example.shop.refunds;

/**
 * Точка входу в процес повернення зі сторони покупця.
 * Приймає запит на повернення і делегує його до RefundService.
 */
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    /** Обробляє клієнтський запит на повернення за замовленням. */
    public String requestRefund(String orderId, long amountCents) {
        return refundService.processRefund(orderId, amountCents);
    }
}