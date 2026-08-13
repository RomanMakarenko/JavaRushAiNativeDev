package com.acme.commerce.refunds;

// Контролер приймання запитів на повернення.
// Endpoint POST /api/refunds — головний кандидат на high-risk area для retry-сценарію.
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    public RefundResult createRefund(RefundRequest request) {
        return refundService.processRefund(request);
    }
}
