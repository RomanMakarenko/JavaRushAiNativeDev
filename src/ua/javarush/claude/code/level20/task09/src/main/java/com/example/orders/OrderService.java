package com.example.orders;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Сервіс оформлення замовлень.
 * ПРОБЛЕМА: бізнес-логіка та надсилання події змішані в одному методі —
 * сервіс напряму тримає ApplicationEventPublisher і сам публікує подію.
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository repository,
                        ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Створює замовлення та публікує подію orders.created.v1.
     * Public API методу змінювати не можна.
     */
    public Order createOrder(NewOrder newOrder) {
        Order order = repository.save(new Order(newOrder.customerId(), newOrder.amount()));

        // побічний ефект прямо в сервісі — інфраструктурне надсилання події
        OrderCreatedEvent event = new OrderCreatedEvent(order.id(), order.customerId(), order.amount());
        eventPublisher.publishEvent(event);

        return order;
    }
}