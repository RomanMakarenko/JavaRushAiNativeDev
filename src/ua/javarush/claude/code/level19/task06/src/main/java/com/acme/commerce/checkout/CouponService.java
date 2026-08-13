package com.acme.commerce.checkout;

import org.springframework.stereotype.Service;

@Service
public class CouponService {

    /**
     * Застосовує купон до суми замовлення і повертає підсумкову суму.
     * Кидає виняток, якщо сума замовлення від'ємна.
     */
    public long applyCoupon(String code, long orderAmount) {
        if (orderAmount < 0) {
            throw new IllegalArgumentException("orderAmount must not be negative");
        }
        long discount = "SAVE10".equals(code) ? orderAmount / 10 : 0;
        return orderAmount - discount;
    }
}