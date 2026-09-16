package com.cashflow.reports;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервіс агрегації виручки. Чиста Java-логіка без імпортів javax.*,
 * без транзакцій і без запису до БД — лише читання.
 */
@Service
public class MonthlyRevenueService {

    public BigDecimal totalRevenue(int year, int month) {
        List<BigDecimal> dailyTotals = loadDailyTotals(year, month);
        return dailyTotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<BigDecimal> loadDailyTotals(int year, int month) {
        // У реальному проєкті — вибірка з read-replica; тут спрощена заглушка.
        return List.of(new BigDecimal("1200.00"), new BigDecimal("980.50"));
    }
}