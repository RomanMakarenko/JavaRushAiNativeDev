package com.example.orders;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Collaborator, який відповідає за інфраструктурне надсилання події замовлення.
 * Side effect винесено сюди з OrderService. Topic і payload не змінюються.
 */
@Component
public class OrderEventPublisher {

    static final String ORDER_CREATED_TOPIC = "orders.created.v1";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Публікує подію про створення замовлення в той самий topic, що й раніше.
     */
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event =
                new OrderCreatedEvent(order.id(), order.customerId(), order.amount());
        kafkaTemplate.send(ORDER_CREATED_TOPIC, String.valueOf(order.id()), event);
    }
}