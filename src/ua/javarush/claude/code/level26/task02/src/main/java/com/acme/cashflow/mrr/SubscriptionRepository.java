package com.acme.cashflow.mrr;

import java.util.List;

/** Доступ до підписок. */
public interface SubscriptionRepository {
    List<Subscription> findAll();
}