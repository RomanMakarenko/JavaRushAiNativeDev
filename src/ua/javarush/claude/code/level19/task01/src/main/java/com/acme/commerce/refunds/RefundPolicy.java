package com.acme.commerce.refunds;

// Правила допустимості повернення (сума, статус платежу тощо).
public class RefundPolicy {

    public void validate(RefundRequest request) {
        if (request.amount() <= 0) {
            throw new IllegalArgumentException("Сума повернення має бути додатною");
        }
    }
}
