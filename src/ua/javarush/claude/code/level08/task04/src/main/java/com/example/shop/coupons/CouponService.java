package com.example.shop.coupons;

/**
 * Сервіс валідації купонів.
 * Перевіряє код купона та повертає результат із розміром знижки.
 */
public class CouponService {

    public CouponResult validate(CouponRequest request) {
        // Рядок 12: за відсутнього code тут падає NPE,
        // бо toUpperCase() викликається на null.
        String normalized = request.code().toUpperCase();
        int discount = normalized.startsWith("SALE") ? 20 : 0;
        return new CouponResult(normalized, discount);
    }
}