package com.example.commerce.refunds;

import java.math.BigDecimal;

/** Сервіс політики повернень: рахує доступну суму refund за замовленням. */
public class RefundPolicyService {

    // Максимальна частка замовлення, доступна до повернення без ручного approval
    private static final BigDecimal AUTO_REFUND_LIMIT = new BigDecimal("0.80");

    public BigDecimal maxAutoRefund(BigDecimal orderTotal) {
        return orderTotal.multiply(AUTO_REFUND_LIMIT);
    }

    public boolean requiresManualApproval(BigDecimal refundAmount, BigDecimal orderTotal) {
        return refundAmount.compareTo(maxAutoRefund(orderTotal)) > 0;
    }
}
