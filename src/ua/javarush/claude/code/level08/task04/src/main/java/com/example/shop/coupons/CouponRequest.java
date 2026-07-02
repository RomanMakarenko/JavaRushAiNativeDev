package com.example.shop.coupons;

/**
 * Запит на валідацію купона.
 */
public record CouponRequest(String code, String cartId) {
}