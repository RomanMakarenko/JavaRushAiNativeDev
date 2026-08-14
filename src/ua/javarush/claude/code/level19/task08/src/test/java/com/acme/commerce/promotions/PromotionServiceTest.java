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

    @Test
    void samePromoAppliedTwiceDoesNotDuplicateDiscount() {
        Cart cart = new Cart();

        service.applyPromotion(cart, "SUMMER10", 1000L);
        service.applyPromotion(cart, "SUMMER10", 1000L);

        // Ідемпотентність за кодом промо: повторне застосування не дублює знижку.
        assertEquals(1000L, cart.getDiscountCents());
    }
}