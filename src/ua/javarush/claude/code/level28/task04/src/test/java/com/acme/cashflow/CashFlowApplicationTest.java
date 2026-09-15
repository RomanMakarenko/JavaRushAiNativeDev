package com.acme.cashflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CashFlowApplicationTest {

    @Test
    void applicationClassExists() {
        // Простий smoke-тест baseline: клас застосунку присутній.
        assertTrue(CashFlowApplication.class.getSimpleName().equals("CashFlowApplication"));
    }
}
