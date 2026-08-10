package com.example.commerce.orders;

// Один рядок замовлення: артикул, кількість і валюта ціни.
public record OrderItem(String sku, int quantity, String currency) {
}
