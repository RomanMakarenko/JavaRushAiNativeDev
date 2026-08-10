package com.example.commerce.orders;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер оформлення замовлення.
 *
 * Поточний diff (issue-432): додано ранню валідацію порожнього кошика —
 * замість падіння з 500 повертається 400 Bad Request із тілом "Cart is empty".
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        List<OrderItem> items = request.items();
        // issue-432: порожній кошик більше не доходить до обчислення суми.
        if (items == null || items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
        }
        return ResponseEntity.ok("Order accepted");
    }
}