package com.acme.commerce.shipping;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PostcodeValidatorTest {

    private final PostcodeValidator validator = new PostcodeValidator();

    @Test
    void acceptsNormalPostcode() {
        assertTrue(validator.isValid("10115"), "Звичайний postcode має бути валідним");
    }

    @Test
    void rejectsPostcodeContainingOnlySpaces() {
        assertFalse(validator.isValid("   "), "Postcode лише з пробілів має бути невалідним");
    }
}