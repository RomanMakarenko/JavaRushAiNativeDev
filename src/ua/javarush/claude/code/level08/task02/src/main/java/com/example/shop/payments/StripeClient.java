package com.example.shop.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeClient {

    // зовнішня інтеграція: ключ читається з оточення через application.yml
    private final String apiKey;

    public StripeClient(@Value("${payments.stripe.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    // списання коштів через зовнішнього платіжного провайдера Stripe
    public ChargeResult charge(ChargeRequest request) {
        // тут був би реальний HTTP-виклик до api.stripe.com
        return ChargeResult.ok();
    }
}