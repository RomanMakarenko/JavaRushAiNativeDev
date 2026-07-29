package com.example.store.coupons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Тести вибірки та конвертації купонів. */
class CouponServiceTest {

    @Test
    void returnsBaseCurrencyAmountWhenCurrencyMatches() {
        CouponRepository repo = () -> List.of(new Coupon("SAVE10", new BigDecimal("10.00"), "USD"));
        RateProvider rates = (from, to) -> BigDecimal.ONE;
        CouponService service = new CouponService(repo, new CurrencyConverter(rates));

        List<CouponView> views = service.findActiveCoupons("USD");

        assertEquals(new BigDecimal("10.00"), views.get(0).getDiscountAmount());
    }

    @Test
    void convertsAmountForForeignCurrency() {
        CouponRepository repo = () -> List.of(new Coupon("SAVE10", new BigDecimal("10.005"), "USD"));
        RateProvider rates = (from, to) -> new BigDecimal("0.90");
        CouponService service = new CouponService(repo, new CurrencyConverter(rates));

        List<CouponView> views = service.findActiveCoupons("EUR");

        // очікування half-up, фактично конвертер використовує half-even
        assertEquals(new BigDecimal("9.01"), views.get(0).getDiscountAmount());
    }
}
