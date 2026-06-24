package com.rush.commerce.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Тест сценарію refresh -> авторизований запит.
public class AuthFlowTest {

    @Test
    void sessionSurvivesTokenRefresh() {
        SessionService service = TestSessions.withActiveSession();
        String refreshToken = TestSessions.knownRefreshToken();

        String newAccessToken = service.rotateAccessToken(refreshToken);

        // Падає: після ротації сесію за новим токеном іноді не знаходить.
        assertTrue(service.isActive(newAccessToken),
                "сесія має залишатися активною після refresh");
    }
}