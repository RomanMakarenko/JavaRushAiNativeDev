package com.acme.cashflow.mrr;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Розрахунок MRR (monthly recurring revenue) за списком підписок.
 * Уся бізнес-логіка нормалізації цін до місяця живе тут, НЕ в контролері.
 */
@Component
public class MrrCalculator {

    /**
     * Сумарний MRR у центах за підписками, активними на дату.
     */
    public long totalMrrCents(List<Subscription> subscriptions, LocalDate on) {
        long total = 0;
        for (Subscription s : subscriptions) {
            total += normalizedMonthlyCents(s);
        }
        return total;
    }

    // Нормалізація ціни підписки до місячної величини.
    // УВАГА: для річних планів ділення цілочисельне — можливе джерело
    // розбіжності MRR із зовнішнім дашбордом (див. inputs/incident.md).
    private long normalizedMonthlyCents(Subscription s) {
        switch (s.billingPeriod()) {
            case MONTHLY:
                return s.priceCents();
            case YEARLY:
                return s.priceCents() / 12;
            default:
                return 0;
        }
    }
}