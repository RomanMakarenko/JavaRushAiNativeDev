package com.cashflow.billing;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

/**
 * Обробник рахунків. High-risk: записує до БД, використовує javax.persistence,
 * транзакції та грошову логіку. Під активною паралельною розробкою.
 */
@Component
public class InvoiceProcessor {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void chargeInvoice(long invoiceId, BigDecimal amount) {
        // Грошова операція зі списанням — критична бізнес-логіка.
        entityManager.createQuery(
                "update Invoice i set i.charged = true, i.amount = :amount where i.id = :id")
                .setParameter("amount", amount)
                .setParameter("id", invoiceId)
                .executeUpdate();
    }
}