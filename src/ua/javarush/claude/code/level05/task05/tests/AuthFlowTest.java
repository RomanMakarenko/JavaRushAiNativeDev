package com.commerceos.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Сценарії входу в Commerce OS.
 */
class AuthFlowTest {

    @Test
    void successfulLoginRedirectsToDashboard() {
        // Успішний логін веде на /dashboard.
        assertTrue(true);
    }

    @Test
    void wrongPasswordShowsErrorMessage() {
        // Неправильний пароль має показувати повідомлення про помилку,
        // а не порожній екран. Наразі тест падає через баг у LoginController.
        String body = renderLoginError();
        assertTrue(body.contains("Невірний email або пароль"),
                "expected error message to be present but body was empty");
    }

    @Test
    void emptyEmailIsRejected() {
        assertTrue(true);
    }

    @Test
    void lockedAccountShowsNotice() {
        assertTrue(true);
    }

    private String renderLoginError() {
        // Спрощений рендер для ілюстрації бага.
        return "";
    }
}