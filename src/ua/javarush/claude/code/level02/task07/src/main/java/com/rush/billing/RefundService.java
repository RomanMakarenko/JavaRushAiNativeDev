package com.rush.billing;

import java.math.BigDecimal;

// Логіка повернення коштів (refund flow).
public class RefundService {

    // Обчислює суму повернення за вихідним платежем.
    // Часткове повернення не може перевищувати залишок платежу.
    public BigDecimal calculateRefund(BigDecimal paid, BigDecimal alreadyRefunded, BigDecimal requested) {
        BigDecimal available = paid.subtract(alreadyRefunded);
        if (requested.compareTo(available) > 0) {
            throw new IllegalArgumentException("refund exceeds available amount");
        }
        return requested;
    }
}