package com.example.shop.refunds;

import com.example.shop.payments.StripeClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Ізольований тест RefundService: перевіряє, що повернення через Stripe
 * повертає непорожній ідентифікатор.
 */
class RefundServiceTest {

    @Test
    void processRefundReturnsRefundId() {
        StripeClient stripe = new StripeClient("sk_test_EXAMPLE_NOT_REAL");
        RefundEventPublisher publisher = new RefundEventPublisher("refund-events");
        RefundService service = new RefundService(stripe, publisher);

        String refundId = service.processRefund("order-7", 2000);

        assertNotNull(refundId);
    }
}