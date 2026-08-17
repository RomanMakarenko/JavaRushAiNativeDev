package com.example.orders;

/**
 * Розрахунок знижки за сумою замовлення. Чим більша сума, тим вищий відсоток знижки.
 */
public class DiscountCalculator {

    // Нечітка назва private-поля: поріг суми, вище якого діє підвищена знижка.
    // Кандидат на локальне перейменування в один refactor-step.
    private final long highDiscountThreshold;

    public DiscountCalculator() {
        this.highDiscountThreshold = 500_00L;
    }

    /**
     * Повертає розмір знижки для заданої суми замовлення.
     */
    public long discountFor(long subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("subtotal must not be negative");
        }
        long rate;
        if (subtotal >= highDiscountThreshold) {
            rate = 10;
        } else {
            rate = 5;
        }
        return subtotal * rate / 100;
    }
}