package com.acme.cashflow.api;

import java.math.BigDecimal;

/**
 * Публічний контракт відповіді зведення MRR.
 * Поле monthlyRecurringRevenue — стабільний зовнішній контракт, змінювати не можна.
 */
public class MrrSummaryResponse {

    private final BigDecimal monthlyRecurringRevenue;
    private final int activeSubscriptions;

    public MrrSummaryResponse(BigDecimal monthlyRecurringRevenue, int activeSubscriptions) {
        this.monthlyRecurringRevenue = monthlyRecurringRevenue;
        this.activeSubscriptions = activeSubscriptions;
    }

    public BigDecimal getMonthlyRecurringRevenue() {
        return monthlyRecurringRevenue;
    }

    public int getActiveSubscriptions() {
        return activeSubscriptions;
    }
}