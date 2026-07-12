package com.example.auth;

/**
 * Контролер автентифікації.
 */
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String login(String user, String password) {
        if (!tokenService.verify(user, password)) {
            throw new AuthException("invalid credentials");
        }
        return tokenService.issueAccessToken(user);
    }

    /**
     * Оновлює access token за допомогою refresh token.
     * УВАГА: refreshToken використовується без перевірки на null і на термін дії —
     * це і є слабке місце поточного diff.
     */
    public String refresh(String refreshToken) {
        String user = tokenService.resolveUser(refreshToken);
        return tokenService.issueAccessToken(user);
    }

}