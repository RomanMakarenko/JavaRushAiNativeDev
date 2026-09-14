package com.rush.billing.subscription;

import com.rush.billing.mrr.BillingPeriod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionServiceTest {

    @Test
    void findActiveIncludesTrialAndActive() {
        SubscriptionService service = new SubscriptionService();
        service.save(new Subscription("a", new BigDecimal("10.00"),
                BillingPeriod.MONTHLY, BigDecimal.ZERO, SubscriptionStatus.ACTIVE));
        service.save(new Subscription("t", new BigDecimal("10.00"),
                BillingPeriod.MONTHLY, BigDecimal.ZERO, SubscriptionStatus.TRIAL));
        service.save(new Subscription("c", new BigDecimal("10.00"),
                BillingPeriod.MONTHLY, BigDecimal.ZERO, SubscriptionStatus.CANCELED));

        assertEquals(2, service.findActive().size());
    }
}