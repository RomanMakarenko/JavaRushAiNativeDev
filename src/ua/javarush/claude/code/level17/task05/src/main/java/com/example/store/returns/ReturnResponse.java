package com.example.store.returns;

// Відповідь на повернення купона.
public record ReturnResponse(String couponCode, boolean refunded) {
}