package com.example.checkout;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Layer 2 quality gate: детерміновані перевірки розрахунку підсумкової суми кошика.
 */
class CheckoutGateTest {

    private final CartTotal cart = new CartTotal();

    @Test
    void totalIncludesDiscount() {
        // 100 - 10 = 90, податок 0% -> 90.00
        BigDecimal result = cart.total(new BigDecimal("100"), new BigDecimal("10"), BigDecimal.ZERO);
        assertEquals(new BigDecimal("90.00"), result);
    }

    @Test
    void totalRoundsHalfUp() {
        // (100 - 0) + 10% = 110, але перевіряємо округлення дробової ставки:
        // 100 * 0.075 = 7.5 -> після обчислення = 107.5 -> 107.50
        BigDecimal result = cart.total(new BigDecimal("100"), BigDecimal.ZERO, new BigDecimal("0.075"));
        assertEquals(new BigDecimal("107.50"), result);
    }

    @Test
    void emptyCartTotalIsZero() {
        BigDecimal result = cart.total(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        assertEquals(new BigDecimal("0.00"), result);
    }
}