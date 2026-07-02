package com.example.shop.refunds;

import com.example.shop.payments.StripeClient;

/**
 * Ядро процесу повернення: поєднує клієнтський запит із зовнішнім платіжним
 * провайдером і публікацією події в чергу.
 *
 * Тут сходяться три точки взаємодії:
 *  - StripeClient   (зовнішній платіжний провайдер),
 *  - RefundEventPublisher (черга подій повернення),
 *  - сам RefundController (вхідна точка).
 */
public class RefundService {

    private final StripeClient stripeClient;
    private final RefundEventPublisher publisher;

    public RefundService(StripeClient stripeClient, RefundEventPublisher publisher) {
        this.stripeClient = stripeClient;
        this.publisher = publisher;
    }

    /** Повертає кошти через Stripe і публікує подію про повернення. */
    public String processRefund(String orderId, long amountCents) {
        String refundId = stripeClient.createRefund(orderId, amountCents);
        publisher.publish(orderId, refundId);
        return refundId;
    }
}