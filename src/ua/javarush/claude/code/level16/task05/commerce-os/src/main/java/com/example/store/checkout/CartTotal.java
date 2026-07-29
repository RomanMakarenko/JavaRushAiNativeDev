package com.example.store.checkout;

import java.math.BigDecimal;

/**
 * Обчислення підсумкової суми кошика.
 * Базова версія з гілки main.
 */
public class CartTotal {

    public BigDecimal total(BigDecimal subtotal, BigDecimal discount, BigDecimal taxRate) {
        // База main: знижка застосовується до subtotal, потім обчислюється податок
        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal tax = afterDiscount.multiply(taxRate);
        return afterDiscount.add(tax);
    }
}