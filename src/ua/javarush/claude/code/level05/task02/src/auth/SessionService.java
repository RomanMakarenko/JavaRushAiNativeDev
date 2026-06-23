package com.rush.commerce.auth;

import org.springframework.stereotype.Service;

/**
 * Сервіс перевірки credentials і керування session.
 * Учора цю гіпотезу як джерело бага відхилили, але в новій session
 * це не гарантується — деталь потрібно принести заново.
 */
@Service
public class SessionService {

    public boolean verify(String username, String password) {
        // Спрощена перевірка credentials
        return password != null && password.length() >= 8;
    }

    public String start(String username) {
        // Створення session token
        return "session-" + username.hashCode();
    }
}