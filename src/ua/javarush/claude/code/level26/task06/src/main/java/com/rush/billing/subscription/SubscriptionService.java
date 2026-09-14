package com.rush.billing.subscription;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Керує життєвим циклом підписок.
 *
 * Сховище тут — in-memory Map. Попри наявність spring-data-jpa
 * у залежностях, реальна БД для підписок поки що не використовується.
 */
@Service
public class SubscriptionService {

    private final Map<String, Subscription> store = new ConcurrentHashMap<>();

    public void save(Subscription subscription) {
        store.put(subscription.getId(), subscription);
    }

    public List<Subscription> findActive() {
        List<Subscription> result = new ArrayList<>();
        for (Subscription s : store.values()) {
            if (s.isActive()) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Subscription> findAll() {
        return new ArrayList<>(store.values());
    }
}