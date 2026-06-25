package com.example.commerce.refund;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контролер refund-flow.
 * У довгій задачі з винесення логіки ухвалення рішення публічний API цього контролера
 * змінювати не можна: шлях, метод і формат запиту/відповіді залишаються тими самими.
 */
@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundPolicyService refundPolicyService;

    public RefundController(RefundPolicyService refundPolicyService) {
        this.refundPolicyService = refundPolicyService;
    }

    @PostMapping("/decision")
    public RefundDecision decide(@RequestBody RefundRequest request) {
        boolean manualApproval = refundPolicyService.requiresManualApproval(request.amount());
        return new RefundDecision(request.orderId(), manualApproval);
    }
}