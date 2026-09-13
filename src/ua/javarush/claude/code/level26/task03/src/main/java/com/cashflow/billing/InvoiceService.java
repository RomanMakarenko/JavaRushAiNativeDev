package com.cashflow.billing;

import java.math.BigDecimal;

/**
 * Сервіс виставлення рахунків. Точка входу для розрахунку суми до сплати.
 */
public class InvoiceService {

    private final TaxCalculator taxCalculator = new TaxCalculator();

    // TODO: винести логіку округлення в окремий helper, дублюється в трьох місцях
    public BigDecimal totalDue(BigDecimal base, String region) {
        BigDecimal tax = taxCalculator.taxFor(base, region);
        return base.add(tax);
    }

    // FIXME: при region == null виникає NPE, потрібна валідація на вході
    public boolean isTaxable(String region) {
        return !region.equalsIgnoreCase("EXEMPT");
    }
}