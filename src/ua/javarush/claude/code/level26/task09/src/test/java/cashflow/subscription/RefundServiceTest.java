package cashflow.subscription;

import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;

/**
 * Один happy-path тест на refund. Edge cases (refund у день білінгу,
 * refund більше залишку) не покриті.
 */
public class RefundServiceTest {

    @Test
    public void partialRefundReducesPrice() {
        Subscription sub = new Subscription(5000L, SubscriptionStatus.ACTIVE);
        RefundService service = new RefundService();
        long applied = service.applyPartialRefund(sub, 1500L, LocalDate.of(2026, 5, 15));
        assertEquals(1500L, applied);
        assertEquals(3500L, sub.getMonthlyPriceCents());
    }
}