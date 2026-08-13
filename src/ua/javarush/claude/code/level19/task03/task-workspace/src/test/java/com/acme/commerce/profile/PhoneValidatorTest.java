package com.acme.commerce.profile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PhoneValidatorTest {

    private final PhoneValidator validator = new PhoneValidator();

    @Test
    void acceptsNormalPhoneNumber() {
        assertTrue(validator.isValid("+49 30 123456"),
                "Звичайний номер телефону має бути дійсним");
    }

    // Готовий failing test (red phase): номер, що складається лише з пробілів
    // має вважатися невалідним.
    @Test
    void rejectsBlankPhoneNumber() {
        assertFalse(validator.isValid("   "),
                "Номер, що складається лише з пробілів, не має проходити валідацію");
    }
}