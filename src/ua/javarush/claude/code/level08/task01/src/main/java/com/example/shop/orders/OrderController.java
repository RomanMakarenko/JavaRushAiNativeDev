package com.example.shop.orders;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // публічна точка входу: створення замовлення з кошика клієнта
    @PostMapping
    OrderResponse create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    // публічна точка входу: отримання замовлення за ідентифікатором
    @GetMapping("/{id}")
    OrderResponse get(@PathVariable String id) {
        return orderService.findById(id);
    }
}