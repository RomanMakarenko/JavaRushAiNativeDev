package com.example.shop.orders;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.refunds.RefundService;

// Контролер приймання замовлень та ініціації повернень.
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RefundService refundService;

    public OrderController(RefundService refundService) {
        this.refundService = refundService;
    }

    // Створення замовлення.
    @PostMapping
    public String create(@RequestBody String order) {
        return "created";
    }

    // Запит на повернення коштів за замовленням делегується в RefundService.
    @PostMapping("/refund")
    public String refund(@RequestBody String orderId) {
        return refundService.processRefund(orderId);
    }
}
