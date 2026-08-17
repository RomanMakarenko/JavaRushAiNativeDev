package com.example.orders;

/** Шлюз до складу. У тестах підмінюється простим test double. */
public interface InventoryGateway {

    int availableStock(String sku);

    void reserve(String sku, int quantity);
}