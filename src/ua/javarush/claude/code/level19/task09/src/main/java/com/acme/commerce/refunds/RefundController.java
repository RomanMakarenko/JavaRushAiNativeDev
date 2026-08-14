package com.acme.commerce.refunds;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    // Приймає запит на повернення коштів по конкретному замовленню.
    @PostMapping("/{id}/refund")
    public ResponseEntity<RefundResult> refund(@PathVariable("id") long orderId,
                                               @RequestBody RefundRequest request) {
        RefundResult result = refundService.requestRefund(orderId, request.amount(), request.reason());
        return ResponseEntity.ok(result);
    }

    // Тіло запиту на повернення.
    public record RefundRequest(long amount, String reason) {
    }
}