package com.example.auth;

/**
 * Сервіс роботи з токенами. У межах задачі цікавить лише те, що метод
 * resolveUser приймає refresh token і повертає користувача.
 */
public interface TokenService {

    boolean verify(String user, String password);

    String issueAccessToken(String user);

    String resolveUser(String refreshToken);
}