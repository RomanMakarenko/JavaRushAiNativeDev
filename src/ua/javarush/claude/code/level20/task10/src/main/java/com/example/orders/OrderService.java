package com.example.orders;

import org.springframework.stereotype.Service;

/**
 * Сервіс оформлення замовлень.
 * Після refactor: надсилання події винесено в collaborator OrderEventPublisher.
 * Public API методу createOrder збережено без змін.
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository repository, OrderEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Створює замовлення та делегує публікацію події collaborator-у.
     * Зовнішній flow і контракт події залишилися незмінними.
     */
    public Order createOrder(NewOrder newOrder) {
        Order order = repository.save(new Order(newOrder.customerId(), newOrder.amount()));
        eventPublisher.publishOrderCreated(order);
        return order;
    }
}