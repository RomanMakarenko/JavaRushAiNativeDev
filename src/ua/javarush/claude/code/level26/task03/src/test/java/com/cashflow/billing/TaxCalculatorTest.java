package com.cashflow.billing;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Робочий тест: перевіряє розрахунок податку за регіоном.
 * Саме його запуск має потрапити до звіту про покриття.
 */
public class TaxCalculatorTest {

    private final TaxCalculator calc = new TaxCalculator();

    @Test
    public void euRateIsTwentyPercent() {
        BigDecimal tax = calc.taxFor(new BigDecimal("100"), "EU");
        assertEquals(new BigDecimal("20.00"), tax);
    }

    @Test
    public void unknownRegionHasZeroTax() {
        BigDecimal tax = calc.taxFor(new BigDecimal("100"), "MARS");
        assertEquals(new BigDecimal("0.00"), tax);
    }
}