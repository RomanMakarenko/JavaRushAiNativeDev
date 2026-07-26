package com.acme.orders;

import java.util.List;

/**
 * Сервіс збірки CSV-експорту замовлень.
 */
public class OrderExportService {

    /**
     * Збирає CSV зі списку замовлень.
     *
     * @param orders список замовлень
     * @return рядок CSV із заголовком і рядками замовлень
     */
    public String toCsv(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,total\n");
        for (Order order : orders) {
            sb.append(order.id()).append(',').append(order.total()).append('\n');
        }
        return sb.toString();
    }
}
