package com.cashflow.reports;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Контролер місячної виручки. Read-only поверхня: лише читання агрегатів,
 * без запису в БД і без грошових операцій. Гарний кандидат для першого
 * migration pilot на Spring Boot 3.x.
 */
@RestController
public class MonthlyRevenueController {

    private final MonthlyRevenueService revenueService;

    public MonthlyRevenueController(MonthlyRevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/api/reports/monthly-revenue")
    public BigDecimal monthlyRevenue(@RequestParam int year, @RequestParam int month) {
        return revenueService.totalRevenue(year, month);
    }
}