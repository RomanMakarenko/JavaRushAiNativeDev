package com.example.orders;

/** Мінімальна модель замовлення для характеризаційних тестів. */
public class Order {

    private final String sku;
    private final boolean paid;

    public Order(String sku, boolean paid) {
        this.sku = sku;
        this.paid = paid;
    }

    public String getSku() {
        return sku;
    }

    public boolean isPaid() {
        return paid;
    }
}