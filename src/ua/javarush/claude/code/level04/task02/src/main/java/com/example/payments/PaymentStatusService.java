package com.example.payments;

// Мапує зовнішній статус шлюзу у внутрішній статус замовлення.
public class PaymentStatusService {

    public void applyExternalStatus(String orderId, String externalStatus) {
        String internal = mapStatus(externalStatus);
        // ... оновлення статусу замовлення у сховищі ...
    }

    private String mapStatus(String externalStatus) {
        // Відомі статуси шлюзу acme-pay: succeeded, failed, pending.
        // BUG: "succeeded" тут не обробляється і потрапляє в UNKNOWN -> PAYMENT_FAILED.
        return switch (externalStatus) {
            case "failed" -> "PAYMENT_FAILED";
            case "pending" -> "PAYMENT_PENDING";
            default -> "PAYMENT_FAILED";
        };
    }
}