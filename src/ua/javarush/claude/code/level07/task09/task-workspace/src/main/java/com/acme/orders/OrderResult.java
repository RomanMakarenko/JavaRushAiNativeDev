package com.acme.orders;

// Результат оформлення замовлення, що повертається клієнту.
public record OrderResult(String status) {
}