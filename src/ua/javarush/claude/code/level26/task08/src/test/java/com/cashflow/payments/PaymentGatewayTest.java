package com.cashflow.payments;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Єдиний тест для payments. Покриває лише happy path charge().
 * verifyWebhook і refund-сценарії не покриті.
 */
class PaymentGatewayTest {

    @Test
    void chargeReturnsReferenceOnHappyPath() {
        PaymentGateway gateway = new PaymentGateway("provider-key", "secret");
        PaymentResult result = gateway.charge("cust-1", 1990);
        assertTrue(result.isSuccess());
        assertNotNull(result.getReference());
    }
}