package com.acme.cashflow.mrr;

import java.math.BigDecimal;

/**
 * Фасад legacy-модуля mrr-engine.
 * Зараз фасад безпосередньо прив'язаний до конкретної JDBC-реалізації lookup
 * (поле типу JdbcPlanLookup). Це заважає ізоляції data read як seam.
 */
public class MrrFacade {

    // пряма залежність від конкретного JDBC-класу — кандидат на seam
    private final JdbcPlanLookup planLookup;

    public MrrFacade(JdbcPlanLookup planLookup) {
        this.planLookup = planLookup;
    }

    /**
     * Повертає місячний MRR за тарифним планом.
     * Public behavior: для наявного плану повертає його monthly_price,
     * для відсутнього — BigDecimal.ZERO.
     */
    public BigDecimal monthlyMrr(String planId) {
        return planLookup.monthlyPrice(planId);
    }
}