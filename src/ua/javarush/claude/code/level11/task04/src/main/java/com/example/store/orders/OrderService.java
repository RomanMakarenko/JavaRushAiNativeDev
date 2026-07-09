package com.example.store.orders;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Бізнес-логіка вибірки замовлень.
 * Повертає замовлення клієнта, відсортовані за датою створення.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Повертає замовлення клієнта, відсортовані за createdAt за зростанням
    public List<Order> findByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .sorted(Comparator.comparing(Order::createdAt))
                .toList();
    }
}