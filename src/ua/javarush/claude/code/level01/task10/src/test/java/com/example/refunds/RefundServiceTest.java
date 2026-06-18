package com.example.refunds;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RefundServiceTest {

    private final RefundService refundService = new RefundService();

    @Test
    void subtractsDiscountFromOrderAmount() {
        BigDecimal refund = refundService.calculateRefund(
                new BigDecimal("100.00"), new BigDecimal("15.00"));
        assertEquals(new BigDecimal("85.00"), refund);
    }

    @Test
    void handlesNullDiscountAsZero() {
        BigDecimal refund = refundService.calculateRefund(
                new BigDecimal("150.00"), null);
        assertEquals(new BigDecimal("150.00"), refund);
    }

    @Test
    void capsRefundAtMaxLimit() {
        BigDecimal refund = refundService.calculateRefund(
                new BigDecimal("20000.00"), new BigDecimal("0.00"));
        assertEquals(new BigDecimal("10000.00"), refund);
    }
}