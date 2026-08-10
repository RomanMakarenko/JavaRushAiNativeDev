package com.example.commerce.orders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void acceptsValidCart() throws Exception {
        String payload = "{\"items\":[{\"sku\":\"A-1\",\"quantity\":2,\"currency\":\"USD\"}]}";

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    // Уже наявний regression test для бага з порожнім кошиком.
    // На поточному коді він червоний; задача — зробити його зеленим мінімальним fix.
    @Test
    void rejectsEmptyCart() throws Exception {
        String payload = "{\"items\":[]}";

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
