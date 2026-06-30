package com.acme.payments;

import com.acme.shared.Money;
import org.springframework.stereotype.Service;

// Обчислення суми повернення на основі Money.
@Service
public class RefundService {

    // Повертає суму до повернення: початкова мінус утримана комісія.
    public Money calculateRefund(Money charged, Money fee) {
        return charged.minus(fee);
    }
}