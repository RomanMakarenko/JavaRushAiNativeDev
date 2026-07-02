package com.example.shop.refunds;

import com.example.shop.payments.StripeClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Наскрізний тест процесу повернення: запит на повернення проходить через
 * RefundController -> RefundService -> StripeClient і публікацію події.
 */
class RefundFlowTest {

    @Test
    void requestRefundReturnsRefundId() {
        StripeClient stripe = new StripeClient("sk_test_EXAMPLE_NOT_REAL");
        RefundEventPublisher publisher = new RefundEventPublisher("refund-events");
        RefundService service = new RefundService(stripe, publisher);
        RefundController controller = new RefundController(service);

        String refundId = controller.requestRefund("order-42", 1500);

        assertNotNull(refundId);
    }
}