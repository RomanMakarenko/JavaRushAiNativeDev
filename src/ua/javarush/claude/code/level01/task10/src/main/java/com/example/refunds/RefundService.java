package com.example.refunds;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Сервіс розрахунку повернень (refunds) за замовленнями.
 */
public class RefundService {

    private static final BigDecimal MAX_REFUND = new BigDecimal("10000.00");

    /**
     * Обчислює суму повернення за ціною замовлення та застосованою знижкою.
     * БАГ: при {@code discount == null} віднімання викидає NullPointerException.
     *
     * @param orderAmount повна сума замовлення
     * @param discount    застосована знижка (може бути {@code null})
     * @return сума до повернення, округлена до двох знаків
     */
    public BigDecimal calculateRefund(BigDecimal orderAmount, BigDecimal discount) {
        // Із суми замовлення віднімається знижка: те, що клієнт реально заплатив, і повертається.
        BigDecimal effectiveDiscount
                = discount != null ? discount : BigDecimal.ZERO;
        BigDecimal refund = orderAmount.subtract(effectiveDiscount);
        if (refund.compareTo(MAX_REFUND) > 0) {
            refund = MAX_REFUND;
        }
        return refund.setScale(2, RoundingMode.HALF_UP);
    }
}