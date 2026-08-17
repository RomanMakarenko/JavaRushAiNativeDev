package com.example.orders;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тест перевіряє, що під час створення замовлення публікується подія
 * orders.created.v1 з коректним payload.
 */
class OrderServiceTest {

    @Test
    void createOrderPublishesOrderCreatedEvent() {
        OrderRepository repository = mock(OrderRepository.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

        when(repository.save(any(Order.class)))
                .thenReturn(new Order(42L, "cust-1", new BigDecimal("100.00")));

        OrderService service = new OrderService(repository, eventPublisher);

        Order created = service.createOrder(new NewOrder("cust-1", new BigDecimal("100.00")));

        assertThat(created.id()).isEqualTo(42L);
        verify(eventPublisher).publishEvent(
                eq(new OrderCreatedEvent(42L, "cust-1", new BigDecimal("100.00"))));
    }
}