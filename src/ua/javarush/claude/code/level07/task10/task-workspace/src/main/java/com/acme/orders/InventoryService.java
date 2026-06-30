package com.acme.orders;

import org.springframework.stereotype.Service;

// Резервування товару на складі під замовлення.
@Service
public class InventoryService {

    public void reserve(String sku, int quantity) {
        // Позначає товар як зарезервований під замовлення.
        // Спричиняє InventoryUnavailableException, якщо товару немає.
    }
}