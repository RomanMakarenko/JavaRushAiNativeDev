package com.acme.commerce.refunds;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefundPolicyTest {

    @Test
    void відхиляєНедодатнуСуму() {
        RefundPolicy policy = new RefundPolicy();
        RefundRequest request = new RefundRequest("key-1", 0);
        assertThrows(IllegalArgumentException.class, () -> policy.validate(request));
    }
}
