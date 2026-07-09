package com.example.store.orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контролер замовлень Commerce OS.
 * Обробляє GET /api/orders і повертає список замовлень клієнта.
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Точка входу GET /api/orders: повертає замовлення за ідентифікатором клієнта
    @GetMapping("/api/orders")
    public List<OrderResponse> listOrders(@RequestParam String customerId) {
        List<Order> orders = orderService.findByCustomer(customerId);
        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }
}