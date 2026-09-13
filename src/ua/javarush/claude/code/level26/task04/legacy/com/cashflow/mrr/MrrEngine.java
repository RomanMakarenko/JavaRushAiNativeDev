package com.cashflow.mrr;

import java.math.BigDecimal;
import java.util.List;

/**
 * Рушій розрахунку MRR (monthly recurring revenue).
 * Найстаріша і найбільш часто виправлювана частина сервісу. Тестів майже немає.
 */
public class MrrEngine {

    // FIXME: метод розрісся, рахує і proration, і churn в одному місці
    public BigDecimal computeMrr(List<BigDecimal> activeSubscriptions, BigDecimal churnAdjustment) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal sub : activeSubscriptions) {
            total = total.add(sub);
        }
        // TODO: churnAdjustment інколи надходить від'ємним, поведінка не визначена
        return total.subtract(churnAdjustment);
    }

    public BigDecimal annualize(BigDecimal mrr) {
        return mrr.multiply(new BigDecimal("12"));
    }
}