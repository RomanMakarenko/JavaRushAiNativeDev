package com.example.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Легкі характеризаційні тести для OrderService.finalizeOrder.
 *
 * Щасливий сценарій уже зафіксовано. Сюди потрібно додати рівно один граничний
 * characterizations test для випадку, коли замовлення оплачене, але залишок дорівнює нулю
 * (`stock = 0`). Продукційний код змінювати не можна; перевіряти private helpers не можна.
 */
class OrderServiceCharacterizationTest {

    /** Проста test double для складу: фіксований залишок, reserve ігнорується. */
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
    void paidOrderWithStockIsConfirmed() {
        OrderService service = new OrderService(new StubInventory(5));
        Order order = new Order("SKU-1", true);

        OrderResult result = service.finalizeOrder(order);

        // Перевіряємо лише спостережуваний результат методу, а не виклики reserveStock.
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals("OK", result.getReason());
    }

    @Test
    void paidOrderWithZeroStockIsBackordered() {
        OrderService service = new OrderService(new StubInventory(0));
        Order order = new Order("SKU-1", true);

        OrderResult result = service.finalizeOrder(order);

        // Перевіряємо лише спостережуваний результат методу, а не виклики reserveStock.
        assertEquals(OrderStatus.BACKORDERED, result.getStatus());
        assertEquals("OUT_OF_STOCK", result.getReason());
    }
}