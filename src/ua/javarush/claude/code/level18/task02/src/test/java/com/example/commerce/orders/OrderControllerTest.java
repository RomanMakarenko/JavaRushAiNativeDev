package com.example.commerce.orders;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    @Test
    void usesDefaultLimitWhenMissing() {
        OrderService service = mock(OrderService.class);
        when(service.findRecent(eq(50))).thenReturn(List.of(new Order("ord-1")));

        OrderController controller = new OrderController(service);

        // limit не передано -> очікуємо значення за замовчуванням 50
        List<Order> result = controller.list(null);

        assertThat(result).hasSize(1);
    }
}