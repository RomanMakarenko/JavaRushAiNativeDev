package com.example.orders;

/**
 * Сховище замовлень. Для навчального сценарію — простий інтерфейс.
 */
public interface OrderRepository {

    Order save(Order order);
}