package com.acme.orders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Прикладна логіка оформлення замовлення: перевірка ліміту, збереження, відповідь.
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final int maxQuantity;

    public OrderService(OrderRepository orderRepository,
                        @Value("${orders.max-quantity:50}") int maxQuantity) {
        this.orderRepository = orderRepository;
        this.maxQuantity = maxQuantity;
    }

    public OrderResult placeOrder(OrderRequest request) {
        // Валідація кількості: гілка помилки при перевищенні ліміту.
        if (request.quantity() > maxQuantity) {
            throw new IllegalArgumentException("Quantity exceeds limit: " + maxQuantity);
        }
        // Перетворення запиту на запис замовлення, що зберігається.
        OrderEntity entity = new OrderEntity(request.sku(), request.quantity(), "CONFIRMED");
        OrderEntity saved = orderRepository.save(entity);
        return new OrderResult(saved.status());
    }
}