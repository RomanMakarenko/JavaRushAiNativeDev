package com.acme.commerce.refunds;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RefundControllerTest {

    @Test
    void создаётВозвратПоОдиночномуЗапросу() {
        RefundController controller = new RefundController(
                new RefundService(new RefundRepository()));
        RefundResult result = controller.createRefund(new RefundRequest("key-1", 500));
        assertEquals("CREATED", result.status());
    }
}