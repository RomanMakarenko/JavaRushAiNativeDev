package com.example.orders;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTest {

    private final OrderService service = new OrderService(new DiscountCalculator());

    @Test
    void appliesDiscountAboveThreshold() {
        // subtotal = 600.00 -> знижка 10% -> підсумок 540.00
        long total = service.total(List.of(new OrderItem(2, 300_00L)));
        assertEquals(540_00L, total);
    }

    @Test
    void appliesLowDiscountBelowThreshold() {
        // subtotal = 100.00 -> знижка 5% -> підсумок 95.00
        long total = service.total(List.of(new OrderItem(1, 100_00L)));
        assertEquals(95_00L, total);
    }

    @Test
    void rejectsEmptyOrder() {
        assertThrows(IllegalArgumentException.class, () -> service.total(List.of()));
    }

    @Test
    void rejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> service.total(List.of(new OrderItem(0, 100_00L))));
    }
}