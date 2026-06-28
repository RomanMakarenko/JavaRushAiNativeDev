package com.acme.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тести login flow.
 * Перевіряють, що після логіну клієнту встановлюється cookie сесії ACME_SESSION.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Успішний логін встановлює cookie серверної сесії.
    @Test
    void login_setsSessionCookie() throws Exception {
        mockMvc.perform(post("/api/login")
                .contentType("application/json")
                .content("{\"username\":\"alice\",\"password\":\"secret\"}"))
               .andExpect(status().isOk())
               .andExpect(cookie().exists("ACME_SESSION"));
    }
}