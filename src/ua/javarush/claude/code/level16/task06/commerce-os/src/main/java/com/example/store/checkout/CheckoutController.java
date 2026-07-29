package com.example.store.checkout;

/**
 * Контролер шару checkout.
 * У цільовій гілці його сигнатуру змінювати не можна:
 * гілка feature/discount-contract чіпає її поза межами завдання.
 */
public class CheckoutController {

    // Поточна стабільна сигнатура (без непов'язаної зміни API)
    public String summary() {
        return "checkout summary";
    }
}