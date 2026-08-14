package com.acme.commerce.checkout;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// API-рівень: @WebMvcTest + MockMvc. Перевіряємо зовнішній HTTP-контракт ендпоінта.
// Сервіс замінено @MockBean — web-slice не піднімає @Service-біни сам.
@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Test
    void appliesCouponForValidOrder() throws Exception {
        when(couponService.applyCoupon("SAVE10", 100)).thenReturn(90L);

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"SAVE10\",\"orderAmount\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(90));
    }

    @Test
    void rejectsNegativeOrderAmountWith400AndErrorBody() throws Exception {
        when(couponService.applyCoupon("SAVE10", -100L))
                .thenThrow(new IllegalArgumentException("orderAmount must not be negative"));

        mockMvc.perform(post("/api/coupons/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"SAVE10\",\"orderAmount\":-100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_ORDER_AMOUNT"));
    }
}