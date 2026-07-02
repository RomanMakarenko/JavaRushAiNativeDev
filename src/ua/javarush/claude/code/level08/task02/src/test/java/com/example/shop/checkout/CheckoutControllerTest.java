package com.example.shop.checkout;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class CheckoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    // контракт: порожній кошик неприпустимий, очікуємо 400
    @Test
    void checkout_returns400_forEmptyCart() throws Exception {
        mockMvc.perform(post("/api/checkout").content("{}"))
               .andExpect(status().isBadRequest());
    }
}