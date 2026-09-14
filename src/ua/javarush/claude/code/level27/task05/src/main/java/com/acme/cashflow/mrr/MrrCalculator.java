package com.acme.cashflow.mrr;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

/**
 * Оркестраційний розрахунок місячного MRR за списком активних підписок.
 * Формула чистого розрахунку захована всередині методу поруч із JDBC та audit —
 * кандидат на виділення чистого ядра CalcCore.
 */
public class MrrCalculator {

    private final JdbcTemplate jdbcTemplate;
    private final AuditSink auditSink;

    public MrrCalculator(JdbcTemplate jdbcTemplate, AuditSink auditSink) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditSink = auditSink;
    }

    /**
     * Обчислює сумарний місячний MRR: кількість активних підписок * ціна плану,
     * з урахуванням знижки. Чиста формула перемішана з data read та audit.
     */
    public BigDecimal totalMonthlyMrr(String planId, int activeSubscriptions, BigDecimal discountRate) {
        // data read
        BigDecimal monthlyPrice = jdbcTemplate.queryForObject(
                "SELECT monthly_price FROM plan WHERE plan_id = ?",
                BigDecimal.class,
                planId);
        if (monthlyPrice == null) {
            monthlyPrice = BigDecimal.ZERO;
        }

        // ЧИСТА формула, захована в orchestration-коді:
        // base = price * subscriptions; total = base * (1 - discount)
        BigDecimal base = monthlyPrice.multiply(new BigDecimal(activeSubscriptions));
        BigDecimal multiplier = BigDecimal.ONE.subtract(discountRate);
        BigDecimal total = base.multiply(multiplier);

        // side effect
        auditSink.record("mrr.total", "planId=" + planId + " total=" + total);

        return total;
    }
}