package cashflow.payment;

/**
 * Логіка повторної спроби оплати після failed payment.
 * Впливає на churn/MRR: успішний retry має відновлювати continuity,
 * але churn dip між failed і retry не відстежується.
 */
public class PaymentRetryService {

    private final int maxRetries;

    public PaymentRetryService(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    // Повтор оплати. Повертає true у разі успіху однієї зі спроб.
    public boolean retry(PaymentAttempt attempt) {
        for (int i = 0; i < maxRetries; i++) {
            if (attempt.charge()) {
                attempt.markResolved();
                return true;
            }
        }
        return false;
    }
}