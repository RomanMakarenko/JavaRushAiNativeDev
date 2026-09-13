package com.acme.cashflow.mrr;

import java.time.LocalDate;

/** Підписка клієнта. */
public record Subscription(
        long id,
        long customerId,
        long priceCents,
        BillingPeriod billingPeriod,
        SubscriptionStatus status,
        LocalDate startedOn
) {
}