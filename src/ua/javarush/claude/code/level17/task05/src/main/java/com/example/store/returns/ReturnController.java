package com.example.store.returns;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/api/coupons/return")
    public ReturnResponse returnCoupon(@RequestBody ReturnRequest request) {
        // Точка входу маршруту повернення купона.
        return returnService.processReturn(request.couponCode());
    }
}