package com.acme.commerce.promotions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acme.commerce.promotions.PromotionService.Cart;
import org.junit.jupiter.api.Test;

class PromotionServiceTest {

    private final PromotionService service = new PromotionService();

    @Test
    void appliesSingleDiscountOnce() {
        Cart cart = new Cart();

        service.applyPromotion(cart, "SUMMER10", 1000L);

        assertEquals(1000L, cart.getDiscountCents());
    }

    // TODO: додати регресійний тест на дублювання знижки
    // (повторний applyPromotion з тим самим промокодом не повинен дублювати знижку).
}