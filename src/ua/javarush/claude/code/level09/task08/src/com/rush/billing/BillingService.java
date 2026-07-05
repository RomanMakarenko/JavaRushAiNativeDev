package com.rush.billing;

import org.springframework.stereotype.Service;

/**
 * Списання коштів за рахунком через платіжний шлюз.
 * На цей момент не перевіряє, чи не було вже успішного платежу за рахунком,
 * через що можливе повторне списання під час ретраю (див. issue BILL-218).
 */
@Service
public class BillingService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient gatewayClient;

    public BillingService(PaymentRepository paymentRepository,
                          PaymentGatewayClient gatewayClient) {
        this.paymentRepository = paymentRepository;
        this.gatewayClient = gatewayClient;
    }

    /** Списати кошти за рахунком і зберегти платіж. */
    public ChargeResult charge(String invoiceId, long amount) {
        String externalId = gatewayClient.createCharge(invoiceId, amount);
        Payment payment = paymentRepository.save(new Payment(externalId, invoiceId, amount, "CHARGED"));
        return new ChargeResult(payment.id(), externalId);
    }
}