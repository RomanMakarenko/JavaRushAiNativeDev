package com.acme.cashflow.mrr;

import org.junit.jupiter.api.Test;

/**
 * TODO: додати parity-перевірку single-plan slice.
 * Проганіть мінімум 3 зафіксовані коди планів через LegacyPlanLookup і
 * PlanLookupV2 та порівняйте однакові observable-поля результату
 * (code, mrr, billingPeriod). Тест не повинен перемикати трафік на новий шлях —
 * він працює напряму з обома реалізаціями.
 *
 * Зафіксовані входи лежать у src/test/resources/fixtures/lookup/plan-codes.csv.
 */
class PlanLookupParityTest {

    @Test
    void placeholder() {
        // Замініть заглушку на реальну parity-перевірку old/new шляхів.
    }
}