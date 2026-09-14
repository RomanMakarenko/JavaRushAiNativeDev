package com.rush.billing.subscription;

import com.rush.billing.mrr.BillingPeriod;

import java.math.BigDecimal;

/**
 * Підписка клієнта. Зберігає суму, період і статус.
 */
public class Subscription {

    private final String id;
    private final BigDecimal amount;
    private final BillingPeriod billingPeriod;
    private final BigDecimal discountRate;
    private SubscriptionStatus status;

    public Subscription(String id,
                        BigDecimal amount,
                        BillingPeriod billingPeriod,
                        BigDecimal discountRate,
                        SubscriptionStatus status) {
        this.id = id;
        this.amount = amount;
        this.billingPeriod = billingPeriod;
        this.discountRate = discountRate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE || status == SubscriptionStatus.TRIAL;
    }

    public boolean isTrial() {
        return status == SubscriptionStatus.TRIAL;
    }
}