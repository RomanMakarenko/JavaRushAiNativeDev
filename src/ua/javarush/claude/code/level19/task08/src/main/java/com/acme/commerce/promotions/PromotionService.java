package com.acme.commerce.promotions;

import java.util.HashSet;
import java.util.Set;

/**
 * Рушій промоакцій: застосовує знижку до кошика.
 *
 * Історична вада: повторний виклик {@code applyPromotion} з тим самим кодом промо
 * дублював знижку (подвійна знижка). Наразі метод є ідемпотентним за кодом промо:
 * повторне застосування одного й того самого промо не змінює підсумкову знижку.
 *
 * Цей production-файл у цій задачі змінювати НЕ потрібно — завдання лише в тому, щоб
 * закрити дефект вузьким regression-тестом.
 */
public class PromotionService {

    /** Кошик із накопиченою знижкою та набором уже застосованих промо. */
    public static final class Cart {
        private long discountCents;
        private final Set<String> appliedPromos = new HashSet<>();

        public long getDiscountCents() {
            return discountCents;
        }

        public Set<String> getAppliedPromos() {
            return appliedPromos;
        }
    }

    /**
     * Застосовує промо до кошика. Ідемпотентно щодо {@code promoCode}: повторний виклик
     * з тим самим кодом не додає знижку ще раз.
     *
     * @param cart       кошик
     * @param promoCode  код промоакції
     * @param amountCents величина знижки в копійках
     */
    public void applyPromotion(Cart cart, String promoCode, long amountCents) {
        // Захист від дублювання знижки: одна й та сама промоакція застосовується один раз.
        if (cart.appliedPromos.contains(promoCode)) {
            return;
        }
        cart.appliedPromos.add(promoCode);
        cart.discountCents += amountCents;
    }
}