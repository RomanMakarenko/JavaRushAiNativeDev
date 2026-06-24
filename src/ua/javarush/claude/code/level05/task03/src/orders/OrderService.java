package com.rush.commerce.orders;

// Сервіс замовлень. До поточного auth-багу стосунку не має.
public class OrderService {

    public Order placeOrder(String userId, Cart cart) {
        // Створення замовлення з кошика користувача.
        Order order = new Order(userId);
        cart.getItems().forEach(order::addItem);
        return order;
    }
}