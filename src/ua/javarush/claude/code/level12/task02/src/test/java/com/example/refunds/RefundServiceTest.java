package com.example.refunds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RefundServiceTest {
    @Test
    void halfPeriodRefundsHalf() {
        assertEquals(50, new RefundService().refundAmount(100, 5, 10));
    }
}