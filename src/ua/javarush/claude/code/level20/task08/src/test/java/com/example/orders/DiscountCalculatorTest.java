package com.example.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountCalculatorTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    @Test
    void highDiscountAtThreshold() {
        // 500.00 -> 10% -> 50.00
        assertEquals(50_00L, calculator.discountFor(500_00L));
    }

    @Test
    void lowDiscountBelowThreshold() {
        // 200.00 -> 5% -> 10.00
        assertEquals(10_00L, calculator.discountFor(200_00L));
    }

    @Test
    void rejectsNegativeSubtotal() {
        assertThrows(IllegalArgumentException.class, () -> calculator.discountFor(-1L));
    }
}