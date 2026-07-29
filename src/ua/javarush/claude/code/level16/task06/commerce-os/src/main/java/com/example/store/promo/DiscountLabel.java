package com.example.store.promo;

/**
 * Текст мітки для promo-знижки, що показується в кошику.
 * Початковий стан цільової гілки (до перенесення безпечної зміни мітки).
 */
public class DiscountLabel {

    public String render(int percent) {
        // Старий текст мітки
        return "Знижка " + percent + "%";
    }
}