package com.example.shop.payments;

/**
 * Основна точка приймання платежів.
 * Точка взаємодії платежів: єдине місце, де створюється charge через StripeClient.
 */
public class PaymentService {

    private final StripeClient stripeClient;

    public PaymentService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    /** Списує кошти з покупця за замовлення. */
    public String charge(long amountCents, String currency) {
        return stripeClient.createCharge(amountCents, currency);
    }
}