package com.example.orders;

/** Спостережуваний результат методу finalizeOrder: статус, reason і message. */
public class OrderResult {

    private final OrderStatus status;
    private final String reason;
    private final String message;

    public OrderResult(OrderStatus status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }
}