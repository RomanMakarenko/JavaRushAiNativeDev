package com.example.store.coupons;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedeemController {

    private final RedeemService redeemService;

    public RedeemController(RedeemService redeemService) {
        this.redeemService = redeemService;
    }

    @PostMapping("/api/coupons/redeem")
    public RedeemResponse redeem(@RequestBody RedeemRequest request) {
        // Точка входу маршруту погашення купона.
        return redeemService.redeem(request.code());
    }
}