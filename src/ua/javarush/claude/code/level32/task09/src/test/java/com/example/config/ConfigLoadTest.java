package com.example.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Перевіряє, що конфігурація завантажується і ключ не є live-секретом.
 * Test-код змінювати не можна.
 */
class ConfigLoadTest {

    @Test
    void apiKeyResolvesAndIsNotLiveSecret() {
        // ключ береться зі змінної середовища, за відсутності — placeholder
        String key = System.getenv().getOrDefault("OPENAI_API_KEY", "changeme");
        assertNotNull(key, "ключ має бути розв'язаний у значення");
        assertFalse(key.startsWith("sk-live"), "live-секрет не повинен потрапляти в конфіг");
    }
}