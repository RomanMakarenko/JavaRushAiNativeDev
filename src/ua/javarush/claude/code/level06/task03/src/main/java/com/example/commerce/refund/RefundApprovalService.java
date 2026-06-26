package com.example.commerce.refund;

/**
 * Сервіс обробки повернень.
 * Вирішує, чи можна автоматично схвалити повернення, чи воно переходить на ручну перевірку.
 */
public class RefundApprovalService {

    // Ліміт суми, вище якого повернення йде на manual review.
    private static final int MANUAL_REVIEW_LIMIT = 250;

    /**
     * Повертає true, якщо повернення можна схвалити автоматично.
     *
     * @param amount сума повернення
     * @return true — автоматичне схвалення, false — потрібна ручна перевірка
     */
    public boolean canAutoApprove(int amount) {
        return amount <= MANUAL_REVIEW_LIMIT;
    }
}