package com.example.shop.checkout;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
class CheckoutController {

    private final CheckoutService checkoutService;

    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    // customer-facing маршрут: оформлення та оплата кошика
    @PostMapping
    CheckoutResponse checkout(@RequestBody CheckoutRequest request) {
        return checkoutService.process(request);
    }
}