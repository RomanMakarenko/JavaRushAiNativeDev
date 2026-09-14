package com.cashflow.payments;

/**
 * Legacy-шлюз приймання платежів. Висока зв'язаність: викликається з MRR,
 * з refund flow і з білінгу. Конфіг провайдера читається з properties.
 */
public class PaymentGateway {

    private final String providerKey;
    private final String webhookSecret;

    public PaymentGateway(String providerKey, String webhookSecret) {
        this.providerKey = providerKey;
        this.webhookSecret = webhookSecret;
    }

    // Проведення списання через зовнішнього провайдера
    public PaymentResult charge(String customerId, long amountCents) {
        if (providerKey == null || providerKey.isBlank()) {
            return PaymentResult.failed("missing-provider-key");
        }
        // тут був би реальний виклик зовнішнього API
        return PaymentResult.ok("chg_" + customerId + "_" + amountCents);
    }

    public boolean verifyWebhook(String signature) {
        return webhookSecret != null && webhookSecret.equals(signature);
    }
}