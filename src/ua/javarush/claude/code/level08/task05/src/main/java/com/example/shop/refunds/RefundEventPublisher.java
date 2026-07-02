package com.example.shop.refunds;

/**
 * Публікує події про повернення у зовнішню чергу.
 * Зовнішня точка взаємодії: брокер повідомлень.
 */
public class RefundEventPublisher {

    private final String queueName;

    public RefundEventPublisher(String queueName) {
        this.queueName = queueName;
    }

    /** Надсилає подію про повернення в чергу. */
    public void publish(String orderId, String refundId) {
        // Реальна відправка в брокер (опущено для навчального прикладу)
    }

    public String getQueueName() {
        return queueName;
    }
}