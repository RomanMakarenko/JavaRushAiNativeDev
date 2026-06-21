package com.example.refunds;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Запит на повернення коштів.
 * Поле notes валідоване через уже підключений стек Bean Validation.
 */
public class RefundRequest {

    @NotNull
    private String orderId;

    // Поточний ліміт довжини нотатки — 500 символів.
    @Size(max = 500, message = "notes must be at most 500 characters")
    private String notes;

    public RefundRequest() {
    }

    public RefundRequest(String orderId, String notes) {
        this.orderId = orderId;
        this.notes = notes;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}