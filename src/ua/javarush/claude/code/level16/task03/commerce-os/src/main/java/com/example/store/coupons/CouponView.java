package com.example.store.coupons;

import java.math.BigDecimal;

/** DTO купона для відповіді API. */
public class CouponView {

    private final String code;
    private final BigDecimal discountAmount;
    private final String currency;

    public CouponView(String code, BigDecimal discountAmount, String currency) {
        this.code = code;
        this.discountAmount = discountAmount;
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public String getCurrency() {
        return currency;
    }
}
