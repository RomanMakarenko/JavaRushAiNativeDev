package com.example.orders;

import org.springframework.stereotype.Service;

/**
 * Сервіс оформлення замовлень.
 * Бізнес-логіка відокремлена від інфраструктури: надсилання події
 * делегується окремому collaborator'у OrderEventPublisher, а сам сервіс
 * більше не тримає ApplicationEventPublisher.
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository repository,
                        OrderEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Створює замовлення та публікує подію orders.created.v1.
     * Public API методу змінювати не можна.
     */
    public Order createOrder(NewOrder newOrder) {
        Order order = repository.save(new Order(newOrder.customerId(), newOrder.amount()));

        // надсилання події делеговано інфраструктурному collaborator'у
        OrderCreatedEvent event = new OrderCreatedEvent(order.id(), order.customerId(), order.amount());
        eventPublisher.publishOrderCreated(event);

        return order;
    }
}