package com.example.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Існуючий сусідній unit test. До методу finalizeOrder не має відношення —
 * перевіряє лише зберігання полів у OrderResult.
 */
class OrderResultTest {

    @Test
    void keepsStatusReasonAndMessage() {
        OrderResult result = new OrderResult(OrderStatus.CONFIRMED, "OK", "Замовлення підтверджено");

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals("OK", result.getReason());
        assertEquals("Замовлення підтверджено", result.getMessage());
    }
}