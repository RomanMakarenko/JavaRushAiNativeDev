package com.example.auth;

/** Помилка автентифікації. */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}