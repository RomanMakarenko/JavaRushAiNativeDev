package com.acme.cashflow.mrr;

import java.math.BigDecimal;
import java.util.List;

/**
 * Legacy-калькулятор MRR (Monthly Recurring Revenue).
 *
 * Метод calculateMonthlyMrr зараз сам шукає тариф за кодом (через
 * виклик findByCode) і відразу використовує знайдений план у розрахунку.
 * Пошук плану і сам розрахунок змішані в одному шарі — це перший кандидат
 * на невеликий behavior-preserving refactor (винести resolvePlan()).
 */
public class MrrCalculator {

    private final List<Plan> plans;

    public MrrCalculator(List<Plan> plans) {
        this.plans = plans;
    }

    /**
     * Обчислює місячний MRR для підписки на тариф planCode.
     * Публічну сигнатуру і текст legacy-винятку чіпати не можна.
     */
    public BigDecimal calculateMonthlyMrr(String planCode, int seats) {
        // Пошук тарифу виконується вбудованим findByCode прямо тут.
        Plan found = null;
        for (Plan plan : plans) {
            if (findByCode(plan, planCode)) {
                found = plan;
                break;
            }
        }
        if (found == null) {
            // Legacy-текст винятку: змінювати не можна, на нього зав'язані тести.
            throw new IllegalArgumentException("Unknown plan code: " + planCode);
        }

        // Розрахунок MRR: ціна за місце множиться на кількість місць.
        return found.getMonthlyPrice().multiply(BigDecimal.valueOf(seats));
    }

    /** Вбудована перевірка відповідності плану коду. */
    private boolean findByCode(Plan plan, String planCode) {
        return plan.getCode().equals(planCode);
    }

    /** Проста модель тарифу. */
    public static class Plan {
        private final String code;
        private final BigDecimal monthlyPrice;

        public Plan(String code, BigDecimal monthlyPrice) {
            this.code = code;
            this.monthlyPrice = monthlyPrice;
        }

        public String getCode() {
            return code;
        }

        public BigDecimal getMonthlyPrice() {
            return monthlyPrice;
        }
    }
}