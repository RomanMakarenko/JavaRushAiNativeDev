package com.rush.commerce.auth;

// Контролер входу та оновлення токена.
public class LoginController {

    private final SessionService sessionService;

    public LoginController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public AuthResponse refresh(String refreshToken) {
        String newAccessToken = sessionService.rotateAccessToken(refreshToken);
        return new AuthResponse(newAccessToken);
    }
}