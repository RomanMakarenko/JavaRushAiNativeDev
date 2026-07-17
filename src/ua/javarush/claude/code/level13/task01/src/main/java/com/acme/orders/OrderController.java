package com.acme.orders;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-вхід для оформлення замовлень Commerce OS.
 * Приймає запит на створення замовлення і делегує обробку в OrderService.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Створення нового замовлення за даними кошика
    @PostMapping
    public OrderResult placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }
}