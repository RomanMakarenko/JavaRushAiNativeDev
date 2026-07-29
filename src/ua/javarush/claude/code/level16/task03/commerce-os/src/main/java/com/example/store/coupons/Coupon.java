package com.example.store.coupons;

import java.math.BigDecimal;

/** Доменна сутність купона. */
public class Coupon {

    private final String code;
    private final BigDecimal discountAmount;
    private final String baseCurrency;

    public Coupon(String code, BigDecimal discountAmount, String baseCurrency) {
        this.code = code;
        this.discountAmount = discountAmount;
        this.baseCurrency = baseCurrency;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }
}
