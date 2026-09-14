package com.acme.cashflow.mrr;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Characterization-тест public behavior MrrFacade.
 * Не залежить від реального JDBC: підміняє lookup тестовою реалізацією.
 */
class MrrFacadeTest {

    /**
     * Тестовий lookup без реального JDBC: перевизначає читання ціни
     * заздалегідь заданою таблицею планів.
     */
    static class FakePlanLookup implements PlanLookup {
        private final Map<String, BigDecimal> prices;

        FakePlanLookup(Map<String, BigDecimal> prices) {
            this.prices = prices;
        }

        @Override
        public BigDecimal monthlyPrice(String planId) {
            return prices.getOrDefault(planId, BigDecimal.ZERO);
        }
    }

    @Test
    void returnsMonthlyPriceForKnownPlan() {
        MrrFacade facade = new MrrFacade(
                new FakePlanLookup(Map.of("pro", new BigDecimal("49.00"))));

        assertEquals(new BigDecimal("49.00"), facade.monthlyMrr("pro"));
    }

    @Test
    void returnsZeroForUnknownPlan() {
        MrrFacade facade = new MrrFacade(
                new FakePlanLookup(Map.of("pro", new BigDecimal("49.00"))));

        assertEquals(BigDecimal.ZERO, facade.monthlyMrr("missing"));
    }
}