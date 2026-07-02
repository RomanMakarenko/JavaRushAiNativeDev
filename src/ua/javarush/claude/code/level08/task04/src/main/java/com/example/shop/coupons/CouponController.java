package com.example.shop.coupons;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер перевірки купонів.
 * Приймає запит на валідацію купона й делегує його в сервіс.
 */
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/api/coupons/validate")
    public ResponseEntity<CouponResult> validate(@RequestBody CouponRequest request) {
        // Зараз відсутній code потрапляє в сервіс і призводить до 500.
        CouponResult result = couponService.validate(request);
        return ResponseEntity.ok(result);
    }
}