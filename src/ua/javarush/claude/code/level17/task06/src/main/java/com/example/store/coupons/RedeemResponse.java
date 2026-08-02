package com.example.store.coupons;

// Відповідь на погашення купона.
public record RedeemResponse(String code, boolean redeemed) {
}