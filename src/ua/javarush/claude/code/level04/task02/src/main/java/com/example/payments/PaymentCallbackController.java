package com.example.payments;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Приймає callback від платіжного шлюзу та оновлює статус замовлення.
@RestController
public class PaymentCallbackController {

    private final PaymentStatusService statusService;

    public PaymentCallbackController(PaymentStatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping("/api/payments/callback")
    public void onCallback(@RequestBody PaymentCallback callback) {
        statusService.applyExternalStatus(callback.orderId(), callback.status());
    }
}