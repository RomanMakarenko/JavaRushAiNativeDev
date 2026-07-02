package com.example.shop.coupons;

/**
 * Результат валідації купона.
 */
public record CouponResult(String code, int discountPercent) {
}