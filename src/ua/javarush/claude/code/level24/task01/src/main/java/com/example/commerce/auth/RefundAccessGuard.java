package com.example.commerce.auth;

/** Перевірка прав доступу до операцій повернення. */
public class RefundAccessGuard {

    // Роль, якій дозволено підтверджувати ручні повернення
    private static final String REFUND_APPROVER_ROLE = "refund-approver";

    public boolean canApproveRefund(String role) {
        return REFUND_APPROVER_ROLE.equals(role);
    }
}
