package com.example.refunds;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тести валідації поля notes.
 * Зараз фіксують старий ліміт 500. Їх потрібно привести до нового ліміту 1000.
 */
class RefundRequestValidationTest {

    private final Validator validator = buildValidator();

    private static Validator buildValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }

    @Test
    void notesAtLimitIsValid() {
        // notes рівно на верхній межі мають проходити валідацію.
        RefundRequest request = new RefundRequest("ORD-1", "a".repeat(500));
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void notesOverLimitIsInvalid() {
        // notes довші за ліміт мають давати порушення.
        RefundRequest request = new RefundRequest("ORD-1", "a".repeat(501));
        assertFalse(validator.validate(request).isEmpty());
    }
}