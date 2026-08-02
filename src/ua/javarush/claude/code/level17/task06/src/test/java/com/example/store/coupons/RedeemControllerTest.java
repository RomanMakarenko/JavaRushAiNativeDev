package com.example.store.coupons;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RedeemControllerTest {

    private final RedeemService redeemService = new RedeemService();

    @Test
    void redeemMarksCouponRedeemed() {
        // Перевіряємо успішне погашення валідного купона.
        RedeemResponse response = redeemService.redeem("SAVE10");
        assertTrue(response.redeemed());
    }

    // Тесту на null/короткий код немає — це прогалина в покритті.
}