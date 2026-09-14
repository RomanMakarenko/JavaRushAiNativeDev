package com.acme.cashflow.mrr;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

/**
 * Фасад legacy-модуля mrr-engine.
 * Наразі знає забагато: напряму звертається до JDBC, пише audit
 * і обробляє refund просто тут. Це й є кандидати на seam.
 */
public class MrrFacade {

    private final JdbcTemplate jdbcTemplate;
    private final AuditSink auditSink;

    public MrrFacade(JdbcTemplate jdbcTemplate, AuditSink auditSink) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditSink = auditSink;
    }

    /**
     * Повертає місячний MRR за тарифним планом.
     * Тут же заховано прямий SQL lookup та побічний audit-ефект.
     */
    public BigDecimal monthlyMrr(String planId) {
        // пряме data read через JDBC — кандидат на seam PlanLookup
        BigDecimal price = jdbcTemplate.queryForObject(
                "SELECT monthly_price FROM plan WHERE plan_id = ?",
                BigDecimal.class,
                planId);

        // побічний ефект audit прямо всередині розрахунку — кандидат на seam
        auditSink.record("mrr.lookup", "planId=" + planId + " price=" + price);

        return price == null ? BigDecimal.ZERO : price;
    }

    /**
     * Грубий розрахунок refund прямо всередині фасаду.
     * refund-логіка тісно змішана з розрахунком — high-risk зона.
     */
    public BigDecimal refundForCancellation(String planId, int unusedDays) {
        BigDecimal monthly = monthlyMrr(planId);
        BigDecimal daily = monthly.divide(new BigDecimal("30"), BigDecimal.ROUND_HALF_UP);
        BigDecimal refund = daily.multiply(new BigDecimal(unusedDays));
        auditSink.record("mrr.refund", "planId=" + planId + " refund=" + refund);
        return refund;
    }
}