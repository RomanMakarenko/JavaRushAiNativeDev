package com.rush.billing;

import org.springframework.stereotype.Repository;

/** Сховище платежів. */
@Repository
public class PaymentRepository {

    /** Зберегти платіж і повернути його з установленим id. */
    public Payment save(Payment payment) {
        // ... збереження в БД
        return payment;
    }
}