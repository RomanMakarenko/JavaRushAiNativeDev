package com.example.shop.payments;

/**
 * Тонка обгортка над зовнішнім платіжним провайдером Stripe.
 * Зовнішня точка взаємодії: реальні мережеві виклики до Stripe API.
 */
public class StripeClient {

    private final String apiKey;

    public StripeClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /** Створює charge на боці Stripe і повертає його ідентифікатор. */
    public String createCharge(long amountCents, String currency) {
        // Реальний мережевий виклик до Stripe (опущено для навчального прикладу)
        return "ch_demo";
    }

    /** Повертає кошти за раніше створений charge — використовується в процесі повернення. */
    public String createRefund(String chargeId, long amountCents) {
        // Реальний мережевий виклик до Stripe (опущено для навчального прикладу)
        return "re_demo";
    }
}