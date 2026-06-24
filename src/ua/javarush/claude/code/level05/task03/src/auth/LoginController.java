package com.rush.commerce.auth;

// Контролер входу та оновлення токена.
public class LoginController {

    private final SessionService sessionService;

    public LoginController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Оновлення access-токена за refresh-токеном.
    public AuthResponse refresh(String refreshToken) {
        String newAccessToken = sessionService.rotateAccessToken(refreshToken);
        // Повертаємо новий токен клієнту.
        return new AuthResponse(newAccessToken);
    }
}