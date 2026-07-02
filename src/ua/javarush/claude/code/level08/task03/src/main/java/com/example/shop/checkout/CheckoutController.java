package com.example.shop.checkout;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер оформлення замовлення.
 * Приймає запит checkout і делегує створення замовлення до сервісного шару.
 */
@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/api/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest request) {
        // Під час невалідних даних очікується 400, але зараз падіння йде в сервіс.
        OrderResponse response = checkoutService.create(request);
        return ResponseEntity.ok(response);
    }
}