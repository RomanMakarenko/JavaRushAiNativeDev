package com.acme.cashflow.subscriptions;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Subscription {

    @Id
    private Long id;

    private String planCode;

    private BigDecimal monthlyPrice;

    public Long getId() {
        return id;
    }

    public String getPlanCode() {
        return planCode;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }
}