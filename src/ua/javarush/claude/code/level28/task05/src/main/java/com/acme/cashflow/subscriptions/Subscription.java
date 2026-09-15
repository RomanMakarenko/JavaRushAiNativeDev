package com.acme.cashflow.subscriptions;

// старі імпорти javax.* — маркер міграції на jakarta.*
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Subscription {

    @Id
    private Long id;

    private String plan;
}