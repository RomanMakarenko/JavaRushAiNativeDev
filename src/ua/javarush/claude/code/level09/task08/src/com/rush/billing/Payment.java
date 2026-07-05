package com.rush.billing;

/** Запис про платіж у billing-модулі. */
public record Payment(String id, String externalId, String invoiceId, long amount, String status) {

    /** Конструктор для нового платежу (id призначається репозиторієм). */
    public Payment(String externalId, String invoiceId, long amount, String status) {
        this(null, externalId, invoiceId, amount, status);
    }
}