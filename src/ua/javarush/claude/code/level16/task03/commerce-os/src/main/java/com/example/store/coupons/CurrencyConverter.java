package com.example.store.coupons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

/** Конвертація сум між валютами з округленням результату. */
@Component
public class CurrencyConverter {

    private final RateProvider rateProvider;

    public CurrencyConverter(RateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    /** Конвертує суму з однієї валюти в іншу та округлює до 2 знаків. */
    public BigDecimal convert(BigDecimal amount, String from, String to) {
        BigDecimal rate = rateProvider.rate(from, to);
        // округлення half-even може розходитися з правилом checkout (half-up)
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }
}
