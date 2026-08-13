package com.example.commerce.orders;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        // Порожній кошик: повертаємо 400 Bad Request (згідно зі схваленим планом).
        if (request.items() == null || request.items().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
        }
        Order order = orderService.place(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}