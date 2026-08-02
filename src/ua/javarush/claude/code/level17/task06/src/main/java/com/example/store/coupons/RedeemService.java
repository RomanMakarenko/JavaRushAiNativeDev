package com.example.store.coupons;

import org.springframework.stereotype.Service;

@Service
public class RedeemService {

    // Бізнес-логіка погашення купона.
    public RedeemResponse redeem(String code) {
        // УВАГА: при code == null тут виникає NullPointerException,
        // який не покрито обробником і доходить до клієнта як 500.
        if (code.length() < 3) {
            throw new IllegalStateException("Занадто короткий код купона");
        }
        return new RedeemResponse(code, true);
    }
}