package com.acme.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер логіну.
 * Логін приймає облікові дані, створює серверну сесію в SessionStore
 * і встановлює клієнту cookie з ідентифікатором сесії.
 */
@RestController
public class AuthController {

    private final SessionStore sessionStore;

    public AuthController(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    // POST /api/login — точка входу login flow.
    @PostMapping("/api/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        // Створюємо серверну сесію і отримуємо її ідентифікатор.
        String sessionId = sessionStore.createSession(request.username());

        // Встановлюємо cookie з ідентифікатором сесії (httpOnly).
        Cookie cookie = new Cookie("ACME_SESSION", sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new LoginResponse("ok");
    }
}