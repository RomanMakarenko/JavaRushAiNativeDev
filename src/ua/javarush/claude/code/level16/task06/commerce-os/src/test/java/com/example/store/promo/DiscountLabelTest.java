package com.example.store.promo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountLabelTest {

    /**
     * Перевіряє, що в цільову гілку перенесено безпечну зміну мітки
     * з гілки feature/discount-label.
     */
    @Test
    void rendersUpdatedLabelText() {
        DiscountLabel label = new DiscountLabel();
        assertEquals("Ваша знижка: 15%", label.render(15));
    }
}