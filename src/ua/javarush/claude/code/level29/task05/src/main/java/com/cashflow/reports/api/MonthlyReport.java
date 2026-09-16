package com.cashflow.reports.api;

import java.math.BigDecimal;
import java.util.List;

/**
 * Структура відповіді monthly report. Контракт зафіксовано в baseline snapshot
 * src/test/resources/baseline/monthly-report.json — змінювати не можна.
 */
public record MonthlyReport(
        String period,
        String currency,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal net,
        List<Category> categories,
        String notes) {

    public record Category(String name, BigDecimal amount) {
    }
}