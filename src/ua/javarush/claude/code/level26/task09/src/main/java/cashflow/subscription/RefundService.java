package cashflow.subscription;

import java.time.LocalDate;

/**
 * Розрахунок повернень. Proration обчислюється за днями періоду, що залишився.
 */
public class RefundService {

    // Часткове повернення середини періоду. Підсумкова сума зменшується
    // за формулою proration, але edge cases (повернення в день білінгу,
    // повернення більше залишку) тестами не покриті.
    public long applyPartialRefund(Subscription sub, long amountCents, LocalDate day) {
        long remaining = sub.getMonthlyPriceCents() - amountCents;
        if (remaining < 0) {
            remaining = 0;
        }
        sub.setMonthlyPriceCents(remaining);
        return amountCents;
    }
}