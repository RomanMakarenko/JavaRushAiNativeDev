package com.example.store.orders;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тести вибірки замовлень клієнта.
 * Перевіряють сортування за createdAt за зростанням.
 */
class OrderServiceTest {

    @Test
    void sortsByCreatedAtAscending() {
        Instant t1 = Instant.parse("2026-01-01T10:00:00Z");
        Instant t2 = Instant.parse("2026-01-02T10:00:00Z");
        OrderRepository repo = customerId -> List.of(
                new Order("o2", "c1", new BigDecimal("20.00"), t2),
                new Order("o1", "c1", new BigDecimal("10.00"), t1)
        );
        OrderService service = new OrderService(repo);
        List<Order> result = service.findByCustomer("c1");
        assertEquals("o1", result.get(0).id());
        assertEquals("o2", result.get(1).id());
    }
}