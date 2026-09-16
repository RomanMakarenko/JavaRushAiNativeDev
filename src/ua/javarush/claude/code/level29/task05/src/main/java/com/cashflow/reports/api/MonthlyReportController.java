package com.cashflow.reports.api;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-рівень monthly report (пілотний обсяг reports).
 * Повертає фіксований звіт, зафіксований у baseline snapshot.
 */
@RestController
public class MonthlyReportController {

    @GetMapping("/api/reports/monthly")
    public MonthlyReport monthly(@RequestParam String period) {
        return new MonthlyReport(
                period,
                "EUR",
                new BigDecimal("18450.00"),
                new BigDecimal("12730.55"),
                new BigDecimal("5719.45"),
                List.of(
                        new MonthlyReport.Category("Salary", new BigDecimal("18000.00")),
                        new MonthlyReport.Category("Interest", new BigDecimal("450.00")),
                        new MonthlyReport.Category("Rent", new BigDecimal("-9000.00")),
                        new MonthlyReport.Category("Groceries", new BigDecimal("-2230.55")),
                        new MonthlyReport.Category("Utilities", new BigDecimal("-1500.00"))),
                null);
    }
}