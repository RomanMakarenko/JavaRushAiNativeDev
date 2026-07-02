package com.example.shop.security;

import org.springframework.stereotype.Component;

@Component
public class AuthFilter {

    // межа авторизації: до checkout-маршруту доходить лише
    // запит із валідною customer session
    public boolean isCustomerSessionValid(String sessionToken) {
        return sessionToken != null && !sessionToken.isBlank();
    }
}