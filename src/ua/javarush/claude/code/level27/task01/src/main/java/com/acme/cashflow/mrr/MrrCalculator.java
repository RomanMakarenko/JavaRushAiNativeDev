package com.acme.cashflow.mrr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Розрахунок Monthly Recurring Revenue (MRR) за підпискою.
 *
 * УВАГА: містить legacy-quirk. Повернення (refund) у поточному місяці поки
 * НЕ зменшує MRR поточного місяця, а переноситься на наступний місяць
 * (carry-forward). Поведінка небажана, але downstream-звіти на неї
 * спираються, тому фіксується characterization-тестом до рефакторингу.
 */
public class MrrCalculator {

    /**
     * Обчислює MRR по місяцях для однієї підписки.
     *
     * @param period       розрахунковий місяць у форматі "YYYY-MM"
     * @param monthlyAmount базова сума підписки за місяць
     * @param events       події підписки (charge, refund, ...)
     * @return мапа "місяць -> MRR"
     */
    public Map<String, Integer> calculate(String period, int monthlyAmount, List<Event> events) {
        Map<String, Integer> mrr = new HashMap<>();
        mrr.put(period, 0);

        for (Event event : events) {
            switch (event.type()) {
                case "charge" -> mrr.merge(period, event.amount(), Integer::sum);
                case "refund" -> {
                    // legacy-quirk: refund поточного місяця переходить у наступний місяць
                    String next = nextPeriod(period);
                    mrr.merge(next, -event.amount(), Integer::sum);
                }
                default -> { /* інші події на MRR тут не впливають */ }
            }
        }
        return mrr;
    }

    /** Повертає наступний місяць у форматі "YYYY-MM". */
    static String nextPeriod(String period) {
        String[] parts = period.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        if (month == 12) {
            return (year + 1) + "-01";
        }
        return year + "-" + String.format("%02d", month + 1);
    }

    /** Подія підписки. */
    public record Event(String type, int amount) {
    }
}