package com.rush.billing.mrr;

import com.rush.billing.subscription.Subscription;
import com.rush.billing.subscription.SubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MrrCalculationServiceTest {

    private final MrrCalculationService service = new MrrCalculationService(new MrrFormulas());

    @Test
    void trialSubscriptionsAreExcludedFromMrr() {
        Subscription paid = new Subscription("s1", new BigDecimal("100.00"),
                BillingPeriod.MONTHLY, BigDecimal.ZERO, SubscriptionStatus.ACTIVE);
        Subscription trial = new Subscription("s2", new BigDecimal("100.00"),
                BillingPeriod.MONTHLY, BigDecimal.ZERO, SubscriptionStatus.TRIAL);

        BigDecimal mrr = service.totalMrr(Arrays.asList(paid, trial));

        assertEquals(new BigDecimal("100.00"), mrr);
    }

    @Test
    void yearlyPlanIsNormalizedToMonthly() {
        Subscription yearly = new Subscription("s3", new BigDecimal("1200.00"),
                BillingPeriod.YEARLY, BigDecimal.ZERO, SubscriptionStatus.ACTIVE);

        BigDecimal mrr = service.totalMrr(Arrays.asList(yearly));

        assertEquals(new BigDecimal("100.00"), mrr);
    }
}