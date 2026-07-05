package com.rush.billing;

import org.springframework.stereotype.Component;

/** Обгортка над зовнішнім платіжним провайдером. */
@Component
public class PaymentGatewayClient {

    /** Створити списання у зовнішнього провайдера, повернути зовнішній id транзакції. */
    public String createCharge(String invoiceId, long amount) {
        // ... виклик зовнішнього провайдера
        return "ext_" + invoiceId;
    }
}