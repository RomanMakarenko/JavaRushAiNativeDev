package com.example.store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Обчислення підсумкової суми замовлення в checkout-модулі.
 * Pending change: додано застосування знижки до проміжної суми.
 */
public class CheckoutService {

    public BigDecimal calculateTotal(List<BigDecimal> lineItems, BigDecimal discountRate) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (BigDecimal item : lineItems) {
            subtotal = subtotal.add(item);
        }

        // Pending change: застосовуємо знижку до проміжної суми
        BigDecimal discount = subtotal.multiply(discountRate);
        BigDecimal total = subtotal.subtract(discount);

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}