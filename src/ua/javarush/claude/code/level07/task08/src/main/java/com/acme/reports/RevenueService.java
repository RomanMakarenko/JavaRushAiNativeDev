package com.acme.reports;

import com.acme.shared.Money;
import org.springframework.stereotype.Service;

import java.util.List;

// Звіт по виручці: агрегує значення Money.
@Service
public class RevenueService {

    // Підсумовує виручку за списком оплат.
    public Money totalRevenue(List<Money> charges) {
        Money total = new Money(0, "USD");
        for (Money charge : charges) {
            total = total.plus(charge);
        }
        return total;
    }
}