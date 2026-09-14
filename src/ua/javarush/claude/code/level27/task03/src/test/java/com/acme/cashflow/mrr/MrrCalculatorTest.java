package com.acme.cashflow.mrr;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Characterization-тести: фіксують поточну поведінку MrrCalculator.
 * Зелені тести = поведінка не змінилася.
 */
class MrrCalculatorTest {

    private MrrCalculator calculator() {
        return new MrrCalculator(List.of(
                new MrrCalculator.Plan("PRO", new BigDecimal("10.00")),
                new MrrCalculator.Plan("TEAM", new BigDecimal("8.50"))
        ));
    }

    @Test
    void calculatesMrrForKnownPlan() {
        // MRR = monthlyPrice * seats
        assertEquals(new BigDecimal("50.00"), calculator().calculateMonthlyMrr("PRO", 5));
    }

    @Test
    void throwsLegacyMessageForUnknownPlan() {
        // Текст legacy-винятку зафіксовано дослівно.
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calculator().calculateMonthlyMrr("GHOST", 3)
        );
        assertEquals("Unknown plan code: GHOST", ex.getMessage());
    }
}