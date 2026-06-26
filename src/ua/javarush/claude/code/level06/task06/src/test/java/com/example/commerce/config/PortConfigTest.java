package com.example.commerce.config;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Smoke test: перевіряє, що стандартний порт сервісу залишається 8080.
 * Поганий коміт змінив порт на 9090 — цей тест має знову стати зеленим після revert.
 */
class PortConfigTest {

    @Test
    void defaultServerPortIs8080() throws Exception {
        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/application.properties")) {
            props.load(in);
        }
        assertEquals("8080", props.getProperty("server.port"));
    }
}