package com.example.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Легкі характеризаційні тести для OrderService.finalizeOrder.
 *
 * Сюди потрібно додати рівно один happy-path characterization test, який
 * фіксує поточний observable result для оплаченого замовлення з доступним залишком.
 * Продуктовий код змінювати не можна; перевіряти private helpers не можна.
 */
class OrderServiceCharacterizationTest {

    /** Простий test double для складу: фіксований залишок, reserve ігнорується. */
    private static class StubInventory implements InventoryGateway {
        private final int stock;

        StubInventory(int stock) {
            this.stock = stock;
        }

        @Override
        public int availableStock(String sku) {
            return stock;
        }

        @Override
        public void reserve(String sku, int quantity) {
            // навмисно нічого не робимо
        }
    }

    @Test
    void confirmsPaidOrderWithAvailableStock() {
        OrderService service = new OrderService(new StubInventory(10));
        Order order = new Order("SKU-123", true);

        OrderResult result = service.finalizeOrder(order);

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals("OK", result.getReason());
    }
}