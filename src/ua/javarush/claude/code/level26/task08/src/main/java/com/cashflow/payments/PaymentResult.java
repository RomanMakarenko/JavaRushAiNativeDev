package com.cashflow.payments;

/** Результат операції платежу. */
public class PaymentResult {

    private final boolean success;
    private final String reference;
    private final String error;

    private PaymentResult(boolean success, String reference, String error) {
        this.success = success;
        this.reference = reference;
        this.error = error;
    }

    public static PaymentResult ok(String reference) {
        return new PaymentResult(true, reference, null);
    }

    public static PaymentResult failed(String error) {
        return new PaymentResult(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReference() {
        return reference;
    }

    public String getError() {
        return error;
    }
}