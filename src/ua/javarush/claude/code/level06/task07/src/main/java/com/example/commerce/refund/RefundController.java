package com.example.commerce.refund;

// Контролер обробки повернень (refund flow).
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    // Приймає запит на повернення і повертає результат обробки.
    public String handleRefund(String orderId) {
        return refundService.processRefund(orderId);
    }
}