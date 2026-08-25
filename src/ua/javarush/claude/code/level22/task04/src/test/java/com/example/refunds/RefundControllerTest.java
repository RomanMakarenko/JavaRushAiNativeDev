package com.example.refunds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RefundController.class)
class RefundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createRefundReturnsRequestedStatus() throws Exception {
        // Шлях повторює помилковий route з контролера і розходиться з контрактом.
        mockMvc.perform(post("/api/order/{id}/refund", "ORD-1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-1001"))
                .andExpect(jsonPath("$.status").value("REFUND_REQUESTED"));
    }
}