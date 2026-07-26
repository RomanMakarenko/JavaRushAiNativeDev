package com.acme.orders;

import java.util.List;

/**
 * Контролер експорту замовлень.
 *
 * Приймає запит на експорт і віддає CSV. Зараз контролер сам збирає
 * CSV-рядки, що робить його перевантаженим.
 */
public class OrderExportController {

    private final OrderExportService exportService;

    public OrderExportController(OrderExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Обробляє запит на експорт замовлень за URL /api/orders/export.
     *
     * @param orders список замовлень для вивантаження
     * @return CSV-представлення списку замовлень
     */
    public String exportOrders(List<Order> orders) {
        return exportService.toCsv(orders);
    }
}
