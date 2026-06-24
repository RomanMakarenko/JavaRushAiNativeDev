package com.rush.commerce.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Падаючий тест: сесія не переживає refresh.
public class AuthFlowTest {

    @Test
    void sessionSurvivesTokenRefresh() {
        SessionService service = TestSessions.withActiveSession();
        String refreshToken = TestSessions.knownRefreshToken();

        String newAccessToken = service.rotateAccessToken(refreshToken);

        assertTrue(service.isActive(newAccessToken),
                "сесія має залишатися активною після refresh");
    }
}