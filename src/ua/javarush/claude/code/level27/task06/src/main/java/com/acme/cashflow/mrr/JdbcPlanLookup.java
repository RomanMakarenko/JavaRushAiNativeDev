package com.acme.cashflow.mrr;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

/**
 * JDBC-реалізація читання ціни тарифного плану.
 * Весь SQL живе тут — це майбутній адаптер за seam PlanLookup.
 */
public class JdbcPlanLookup {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPlanLookup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal monthlyPrice(String planId) {
        BigDecimal price = jdbcTemplate.queryForObject(
                "SELECT monthly_price FROM plan WHERE plan_id = ?",
                BigDecimal.class,
                planId);
        return price == null ? BigDecimal.ZERO : price;
    }
}