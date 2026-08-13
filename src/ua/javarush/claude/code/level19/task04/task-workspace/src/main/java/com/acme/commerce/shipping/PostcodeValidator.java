package com.acme.commerce.shipping;

/**
 * Валідатор поштового індексу адреси доставки.
 *
 * Відомий баг: рядок лише з пробілів проходить перевірку як валідний
 * postcode, тому що метод не робить trim перед перевіркою на порожнечу.
 */
public class PostcodeValidator {

    /**
     * Повертає true, якщо postcode валідний.
     *
     * @param postcode поштовий індекс
     * @return результат валідації
     */
    public boolean isValid(String postcode) {
        // Баг: рядок із пробілів проходить як валідний (немає trim)
        return postcode != null && !postcode.isEmpty();
    }
}