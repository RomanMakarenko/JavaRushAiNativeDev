package com.acme.orders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

// Сховище замовлень (in-memory для навчального сервісу).
@Repository
public class OrderRepository {

    private final ConcurrentHashMap<Long, OrderEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public OrderEntity save(OrderEntity entity) {
        long id = sequence.incrementAndGet();
        store.put(id, entity);
        return entity;
    }
}