package com.acme.orders;

import com.acme.shared.Money;
import org.springframework.stereotype.Service;

import java.util.List;

// Оформлення замовлення: підсумовує позиції в Money.
@Service
public class OrderService {

    // Обчислює підсумок замовлення як суму позицій.
    public Money calculateTotal(List<Money> lineItems) {
        Money total = new Money(0, "USD");
        for (Money item : lineItems) {
            total = total.plus(item);
        }
        return total;
    }
}