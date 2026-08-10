package com.example.commerce.orders;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class OrderControllerTest {

    private final OrderService orderService = mock(OrderService.class);
    private final OrderController controller = new OrderController(orderService);

    @Test
    void returnsBadRequestForEmptyCart() {
        CreateOrderRequest request = new CreateOrderRequest(List.of());

        ResponseEntity<?> response = controller.createOrder(request);

        // Схвалений план: порожній кошик має повертати 400 Bad Request.
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cart is empty", response.getBody());
    }
}