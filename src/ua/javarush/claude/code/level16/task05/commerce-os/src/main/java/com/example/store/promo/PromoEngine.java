package com.example.store.promo;

import java.math.BigDecimal;

/**
 * Обчислення promo-знижки для кошика.
 * Базова версія з гілки main.
 */
public class PromoEngine {

    public BigDecimal discountFor(BigDecimal subtotal, String promoCode) {
        // База main: фіксована знижка 10% за будь-яким непорожнім промокодом
        if (promoCode == null || promoCode.isBlank()) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(new BigDecimal("0.10"));
    }
}