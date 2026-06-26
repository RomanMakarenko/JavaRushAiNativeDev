package com.example.commerce.refund;

// Сервіс розрахунку та проведення повернення.
public class RefundService {

    // Проводить повернення за ідентифікатором замовлення.
    public String processRefund(String orderId) {
        return "refund-accepted:" + orderId;
    }
}