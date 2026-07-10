package com.example.refunds;

/** Сервіс розрахунку повернень (production-код, змінювати агентам не можна). */
public class RefundService {
    public long refundAmount(long paid, int usedDays, int totalDays) {
        if (totalDays <= 0) {
            throw new IllegalArgumentException("totalDays must be positive");
        }
        long remaining = (long) totalDays - usedDays;
        if (remaining < 0) remaining = 0;
        return paid * remaining / totalDays;
    }
}