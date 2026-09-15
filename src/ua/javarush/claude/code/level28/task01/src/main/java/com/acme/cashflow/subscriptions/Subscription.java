package com.acme.cashflow.subscriptions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// Сутність підписки. Використовує старий namespace javax.persistence
// (Spring Boot 2.7 / Java EE), який під час migration на Boot 3.x
// має перейти в jakarta.persistence.
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    private Long id;

    private String plan;

    private String status;

    public Long getId() {
        return id;
    }

    public String getPlan() {
        return plan;
    }

    public String getStatus() {
        return status;
    }
}