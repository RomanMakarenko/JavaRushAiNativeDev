package com.example.commerce.orders;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    // Оформлює замовлення для непорожнього кошика.
    public Order place(CreateOrderRequest request) {
        return new Order(UUID.randomUUID().toString(), request.items());
    }
}