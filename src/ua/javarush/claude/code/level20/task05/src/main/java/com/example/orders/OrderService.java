package com.example.orders;

/**
 * Сервіс фіналізації замовлення.
 *
 * Перед локальним refactor цього класу команда хоче зафіксувати
 * поточну observable поведінку методу {@link #finalizeOrder} за допомогою
 * lightweight characterization tests, а не переписувати тестовий шар.
 */
public class OrderService {

    private final InventoryGateway inventory;

    public OrderService(InventoryGateway inventory) {
        this.inventory = inventory;
    }

    /**
     * Фіналізує замовлення та повертає observable result.
     *
     * Поточна поведінка (як є, без оцінки business correctness):
     * - неоплачене замовлення -> статус REJECTED, reason PAYMENT_REQUIRED;
     * - оплачене замовлення за наявності залишку -> статус CONFIRMED;
     * - оплачене замовлення за нульового залишку -> статус BACKORDERED.
     */
    public OrderResult finalizeOrder(Order order) {
        if (!order.isPaid()) {
            return new OrderResult(
                    OrderStatus.REJECTED,
                    "PAYMENT_REQUIRED",
                    "Замовлення не оплачено");
        }

        int stock = inventory.availableStock(order.getSku());
        if (stock <= 0) {
            return new OrderResult(
                    OrderStatus.BACKORDERED,
                    "OUT_OF_STOCK",
                    "Товар тимчасово недоступний");
        }

        reserveStock(order, stock);
        return new OrderResult(
                OrderStatus.CONFIRMED,
                "OK",
                "Замовлення підтверджено");
    }

    // Приватний helper: характеризаційні тести НЕ повинні перевіряти його виклики.
    private void reserveStock(Order order, int stock) {
        inventory.reserve(order.getSku(), 1);
    }
}