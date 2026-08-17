package com.example.orders;

/**
 * Сервіс фіналізації замовлення.
 *
 * Перед локальним рефакторингом цього класу команда хоче зафіксувати
 * поточну спостережувану поведінку методу {@link #finalizeOrder} за допомогою
 * легких характеризаційних тестів, а не переписувати тестовий шар.
 */
public class OrderService {

    private final InventoryGateway inventory;

    public OrderService(InventoryGateway inventory) {
        this.inventory = inventory;
    }

    /**
     * Фіналізує замовлення і повертає спостережуваний результат.
     *
     * Поточна поведінка (як є, без оцінки коректності бізнес-логіки):
     * - неоплачене замовлення -> статус REJECTED, reason PAYMENT_REQUIRED;
     * - оплачений замовлення при доступному залишку -> статус CONFIRMED;
     * - оплачений замовлення при нульовому залишку -> статус BACKORDERED.
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

    // Приватний helper: характеризаційні тести НЕ мають перевіряти його виклики.
    private void reserveStock(Order order, int stock) {
        inventory.reserve(order.getSku(), 1);
    }
}