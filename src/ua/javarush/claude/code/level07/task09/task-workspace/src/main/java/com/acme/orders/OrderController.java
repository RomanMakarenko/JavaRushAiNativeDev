package com.acme.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// HTTP точка входу для оформлення замовлення: POST /api/orders.
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResult> placeOrder(@RequestBody OrderRequest request) {
        // Делегує прикладну логіку в OrderService.
        OrderResult result = orderService.placeOrder(request);
        return ResponseEntity.ok(result);
    }
}