package com.acme.commerce.profile;

/**
 * Валідатор номера телефону в профілі покупця.
 */
public class PhoneValidator {

    /**
     * Повертає true, якщо номер телефону дійсний.
     *
     * @param phone номер телефону
     * @return результат валідації
     */
    public boolean isValid(String phone) {
        return phone != null && !phone.trim().isEmpty();
    }
}