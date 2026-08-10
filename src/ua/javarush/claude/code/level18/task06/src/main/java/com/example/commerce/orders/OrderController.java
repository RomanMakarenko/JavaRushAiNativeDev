package com.example.commerce.orders;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // БАГ: порожній кошик не перевіряється. items.get(0) на порожньому списку
    // кидає IndexOutOfBoundsException і ендпоінт відповідає 500 замість 400.
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody OrderRequest request) {
        List<OrderItem> items = request.items();

        // Порожній кошик: замовлення неможливе. Повертаємо 400 замість 500.
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "BAD_REQUEST",
                    "message", "Cart is empty"
            ));
        }

        // Беремо перший товар, щоб визначити валюту замовлення.
        OrderItem first = items.get(0);
        String currency = first.currency();

        Map<String, Object> body = Map.of(
                "status", "CREATED",
                "currency", currency,
                "lineCount", items.size()
        );
        return ResponseEntity.ok(body);
    }
}
