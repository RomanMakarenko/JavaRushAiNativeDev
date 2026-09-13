package com.cashflow.billing;

import java.math.BigDecimal;

/**
 * Розрахунок податку за регіоном. Ставки захардкоджені — застарілий підхід.
 */
public class TaxCalculator {

    // TODO: ставки мають надходити з конфігурації, а не з коду
    public BigDecimal taxFor(BigDecimal base, String region) {
        BigDecimal rate;
        if ("EU".equalsIgnoreCase(region)) {
            rate = new BigDecimal("0.20");
        } else if ("US".equalsIgnoreCase(region)) {
            rate = new BigDecimal("0.07");
        } else {
            rate = new BigDecimal("0.00");
        }
        return base.multiply(rate);
    }
}