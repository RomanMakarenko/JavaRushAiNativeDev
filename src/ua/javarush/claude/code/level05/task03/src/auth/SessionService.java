package com.rush.commerce.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Сервіс керування сесіями та ротацією токенів.
public class SessionService {

    // Активні сесії: ключ — access-токен.
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    // Ротація access-токена за refresh-токеном.
    public String rotateAccessToken(String refreshToken) {
        Session session = findByRefresh(refreshToken);
        String newAccessToken = TokenFactory.newAccessToken();

        // УВАГА: стара запис сесії видаляється до того, як додано нову.
        // Якщо паралельний запит надійде між цими рядками — сесія "втрачається".
        sessions.remove(session.getAccessToken());
        session.setAccessToken(newAccessToken);
        sessions.put(newAccessToken, session);
        return newAccessToken;
    }

    private Session findByRefresh(String refreshToken) {
        return sessions.values().stream()
                .filter(s -> s.getRefreshToken().equals(refreshToken))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("session not found"));
    }
}