package com.example.orders;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тест після refactor: сервіс делегує публікацію події collaborator-у.
 * Поведінка createOrder збережена, перевіряється виклик publishOrderCreated.
 */
class OrderServiceTest {

    @Test
    void createOrderDelegatesEventPublishingToCollaborator() {
        OrderRepository repository = mock(OrderRepository.class);
        OrderEventPublisher eventPublisher = mock(OrderEventPublisher.class);

        Order saved = new Order(42L, "cust-1", new BigDecimal("100.00"));
        when(repository.save(any(Order.class))).thenReturn(saved);

        OrderService service = new OrderService(repository, eventPublisher);

        Order created = service.createOrder(new NewOrder("cust-1", new BigDecimal("100.00")));

        assertThat(created.id()).isEqualTo(42L);
        verify(eventPublisher).publishOrderCreated(saved);
    }
}