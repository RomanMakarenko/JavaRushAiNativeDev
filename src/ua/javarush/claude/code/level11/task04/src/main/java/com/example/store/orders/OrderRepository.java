package com.example.store.orders;

import java.util.List;

/**
 * Джерело даних для замовлень.
 * Реалізація підключається через Spring; тут лише контракт вибірки.
 */
public interface OrderRepository {

    // Повертає замовлення конкретного клієнта
    List<Order> findByCustomerId(String customerId);
}