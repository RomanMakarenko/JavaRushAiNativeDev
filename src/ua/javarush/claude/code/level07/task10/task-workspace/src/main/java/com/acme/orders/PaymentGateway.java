package com.acme.orders;

import org.springframework.stereotype.Service;

// Авторизація платежу у зовнішньому платіжному шлюзі.
@Service
public class PaymentGateway {

    public String authorize(String paymentToken, int amount) {
        // Авторизує списання та повертає ідентифікатор авторизації.
        return "auth-" + paymentToken;
    }
}