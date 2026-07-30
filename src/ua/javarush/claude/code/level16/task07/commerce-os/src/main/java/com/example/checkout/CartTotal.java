package com.example.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Розрахунок підсумкової суми кошика checkout-модуля.
 * Покрито тестами CheckoutGate (Layer 2 quality gate).
 */
public class CartTotal {

    /**
     * Підсумкова сума: (subtotal - discount) + податок, округлення HALF_UP до 2 знаків.
     */
    public BigDecimal total(BigDecimal subtotal, BigDecimal discount, BigDecimal taxRate) {
        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal tax = afterDiscount.multiply(taxRate);
        return afterDiscount.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
}