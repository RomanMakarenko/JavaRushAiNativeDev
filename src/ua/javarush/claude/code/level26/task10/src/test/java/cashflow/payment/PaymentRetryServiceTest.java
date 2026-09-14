package cashflow.payment;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Покриття retry лише на happy-path: одна успішна спроба.
 * Churn dip між failed і retry не перевіряється.
 */
public class PaymentRetryServiceTest {

    @Test
    public void retrySucceedsOnSecondAttempt() {
        PaymentAttempt attempt = new PaymentAttempt(2);
        PaymentRetryService service = new PaymentRetryService(3);
        assertTrue(service.retry(attempt));
    }
}