package com.example.commerce.orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Поточний стан до кроку 1: limit передається як є, значення за замовчуванням поки немає.
    @GetMapping
    public List<Order> list(@RequestParam Integer limit) {
        return orderService.findRecent(limit);
    }
}