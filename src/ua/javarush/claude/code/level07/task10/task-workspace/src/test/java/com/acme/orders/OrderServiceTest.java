package com.acme.orders;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// Тест оформлення замовлення. Зараз перевіряє лише результат,
// але не фіксує порядок викликів reserve -> authorize -> save.
class OrderServiceTest {

    @Test
    void placeOrderReturnsConfirmed() {
        OrderService service = new OrderService(
                new InventoryService(), new PaymentGateway(), new OrderRepository());

        OrderResult result = service.placeOrder(new OrderRequest("SKU-1", 2, "tok-1"));

        assertThat(result.status()).isEqualTo("CONFIRMED");
    }
}