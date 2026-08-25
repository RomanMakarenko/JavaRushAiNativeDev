package com.example.refunds;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RefundController {

    // УВАГА: шлях містить помилку (/api/order замість /api/orders)
    // і розходиться з контрактом із docs/api/refunds.md.
    @PostMapping("/api/order/{id}/refund")
    public ResponseEntity<Map<String, String>> createRefund(@PathVariable String id) {
        Map<String, String> body = Map.of(
                "orderId", id,
                "status", "REFUND_REQUESTED"
        );
        return ResponseEntity.ok(body);
    }
}