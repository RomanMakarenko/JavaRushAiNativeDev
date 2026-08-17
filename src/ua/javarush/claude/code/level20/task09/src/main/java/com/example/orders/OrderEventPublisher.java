package com.example.orders;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Інфраструктурний компонент, який відповідає за публікацію події
 * про створення замовлення у topic "orders.created.v1".
 * Приховує деталі надсилання події від бізнес-логіки OrderService.
 */
@Component
public class OrderEventPublisher {

    /** Ім'я topic, у який публікується подія створення замовлення. */
    public static final String ORDER_CREATED_TOPIC = "orders.created.v1";

    private final ApplicationEventPublisher eventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Публікує подію створення замовлення у topic "orders.created.v1".
     * Payload (OrderCreatedEvent) — зовнішній контракт, його не міняємо.
     */
    public void publishOrderCreated(OrderCreatedEvent event) {
        eventPublisher.publishEvent(event);
    }
}