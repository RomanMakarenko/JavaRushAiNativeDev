package com.acme.orders;

/**
 * Замовлення для вивантаження в CSV.
 *
 * @param id    ідентифікатор замовлення
 * @param total сума замовлення в мінімальних одиницях валюти
 */
public record Order(long id, long total) {
}
