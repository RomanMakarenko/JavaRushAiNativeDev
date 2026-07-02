package com.example.shop.checkout;

/**
 * Сервіс оформлення замовлення.
 * Тут відбувається фактичне створення замовлення з даних запиту.
 */
public class CheckoutService {

    public OrderResponse create(CheckoutRequest request) {
        // Падіння відбувається тут: customerEmail може бути null,
        // а код одразу викликає trim() без перевірки.
        String email = request.customerEmail().trim();
        return new OrderResponse("ORDER-1001", email);
    }
}