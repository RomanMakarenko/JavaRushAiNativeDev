package com.example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Наявні тести публічного контракту сервісу.
 * Файл чіпати не можна: він фіксує спостережувану поведінку до та після рефакторингу.
 */
class OrderServiceTest {

    @Test
    void placeOrderReturnsPlacedStatus() {
        OrderService service = new OrderService();
        OrderResponse response = service.placeOrder(new OrderRequest("A-1", new BigDecimal("100.00")));
        assertEquals("A-1", response.orderId());
        assertEquals("PLACED", response.status());
        assertEquals(new BigDecimal("100.00"), response.amount());
    }

    @Test
    void placeOrderRejectsNonPositiveAmount() {
        OrderService service = new OrderService();
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(new OrderRequest("A-2", BigDecimal.ZERO)));
    }

    @Test
    void cancelOrderChangesStatus() {
        OrderService service = new OrderService();
        service.placeOrder(new OrderRequest("A-3", new BigDecimal("50.00")));
        OrderResponse cancelled = service.cancelOrder("A-3");
        assertEquals("CANCELLED", cancelled.status());
    }
}