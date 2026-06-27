package com.example.commerce.refund;

/**
 * Сервіс обробки повернень (refund-flow).
 */
public class RefundService {

    /**
     * Визначає, чи можна провести повернення.
     *
     * @param amount  сума повернення
     * @param manualReview чи переходить повернення на ручну перевірку
     * @param comment коментар оператора
     * @return true, якщо повернення дозволено
     */
    public boolean canRefund(int amount, boolean manualReview, String comment) {
        if (amount <= 0) {
            return false;
        }
        // Для manual review потрібен непорожній коментар.
        if (manualReview && (comment == null || comment.isBlank())) {
            return false;
        }
        return true;
    }
}