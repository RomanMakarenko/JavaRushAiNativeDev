package com.acme.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Серверне сховище сесій.
 * Сесія живе на сервері: ідентифікатор генерується тут, а клієнту
 * віддається лише цей ідентифікатор через cookie. Жодного JWT і зберігання
 * токена на клієнті немає.
 */
@Component
public class SessionStore {

    // sessionId -> username. In-memory сховище серверних сесій.
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    /** Створює нову серверну сесію і повертає її ідентифікатор. */
    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }

    /** Повертає username за ідентифікатором сесії або null. */
    public String resolveUser(String sessionId) {
        return sessions.get(sessionId);
    }
}