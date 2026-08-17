package com.example.orders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Бізнес-логіка замовлень. Публічні методи утворюють контракт сервісу,
 * private-методи — внутрішня реалізація, яку можна реорганізувати.
 */
public class OrderService {

    private final Map<String, OrderResponse> storage = new HashMap<>();

    public OrderResponse placeOrder(OrderRequest request) {
        processOrderChecks(request);
        OrderResponse response = new OrderResponse(
                request.orderId(),
                "PLACED",
                request.amount());
        storage.put(request.orderId(), response);
        return response;
    }

    public OrderResponse findOrder(String orderId) {
        OrderResponse found = storage.get(orderId);
        if (found == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return found;
    }

    public OrderResponse cancelOrder(String orderId) {
        OrderResponse found = findOrder(orderId);
        OrderResponse cancelled = new OrderResponse(found.orderId(), "CANCELLED", found.amount());
        storage.put(orderId, cancelled);
        return cancelled;
    }

    // Внутрішня перевірка коректності замовлення перед збереженням.
    private void processOrderChecks(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.orderId() == null || request.orderId().isBlank()) {
            throw new IllegalArgumentException("Order id must not be blank");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}