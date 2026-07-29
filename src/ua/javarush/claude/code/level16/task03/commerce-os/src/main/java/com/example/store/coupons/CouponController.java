package com.example.store.coupons;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST-вхід для роботи з купонами. */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /** Повертає активні купони, опціонально фільтруючи за валютою замовлення. */
    @GetMapping
    public List<CouponView> listCoupons(@RequestParam(required = false) String currency) {
        return couponService.findActiveCoupons(currency);
    }
}
