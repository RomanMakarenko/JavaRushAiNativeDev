package com.acme.orders;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// Тест маршруту оформлення замовлення: фіксує успішний сценарій.
class OrderControllerTest {

    @Test
    void placeOrderReturnsConfirmedStatus() {
        OrderRepository repository = new OrderRepository();
        OrderService service = new OrderService(repository, 50);
        OrderController controller = new OrderController(service);

        OrderRequest request = new OrderRequest("SKU-1", 2, "tok-test");
        OrderResult result = controller.placeOrder(request).getBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo("CONFIRMED");
    }
}