package com.acme.commerce.profile;

/**
 * Валідатор номера телефону в профілі покупця.
 *
 * Відомий баг: номер, що складається лише з пробілів, проходить перевірку як дійсний,
 * тому що метод не виконує trim перед перевіркою на порожнечу.
 */
public class PhoneValidator {

    /**
     * Повертає true, якщо номер телефону дійсний.
     *
     * @param phone номер телефону
     * @return результат валідації
     */
    public boolean isValid(String phone) {
        // Баг: номер із пробілів проходить як дійсний (немає trim)
        return phone != null && !phone.isEmpty();
    }
}