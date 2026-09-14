package com.rush.billing.mrr;

import com.rush.billing.subscription.Subscription;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Нормалізація суми підписки до місячного еквівалента.
 */
@Component
public class MrrFormulas {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    /**
     * Приводить суму підписки до місячного значення.
     * Річні плани діляться на 12, місячні беруться як є.
     * Знижка застосовується ПІСЛЯ нормалізації (особливість legacy-розрахунку).
     */
    public BigDecimal monthlyAmount(Subscription s) {
        BigDecimal base = s.getAmount();
        if (s.getBillingPeriod() == BillingPeriod.YEARLY) {
            base = base.divide(MONTHS_IN_YEAR, 2, RoundingMode.HALF_UP);
        }
        BigDecimal discountFactor = BigDecimal.ONE.subtract(s.getDiscountRate());
        return base.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }
}