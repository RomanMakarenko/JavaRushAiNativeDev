package com.example.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести покривають лише login.
 * Для нового методу refresh тесту поки немає — це видно в поточному diff.
 */
class AuthControllerTest {

    @Test
    void loginReturnsTokenForValidCredentials() {
        TokenService svc = new FakeTokenService(true);
        AuthController controller = new AuthController(svc);
        assertNotNull(controller.login("alice", "secret"));
    }

    @Test
    void loginRejectsInvalidCredentials() {
        TokenService svc = new FakeTokenService(false);
        AuthController controller = new AuthController(svc);
        assertThrows(AuthException.class, () -> controller.login("alice", "wrong"));
    }
}