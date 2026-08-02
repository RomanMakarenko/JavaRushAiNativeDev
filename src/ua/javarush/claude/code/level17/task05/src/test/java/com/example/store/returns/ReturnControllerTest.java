package com.example.store.returns;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ReturnControllerTest {

    private final ReturnService returnService = new ReturnService();

    @Test
    void processReturnMarksCouponRefunded() {
        // Перевіряємо успішне повернення валідного купона.
        ReturnResponse response = returnService.processReturn("SAVE10");
        assertTrue(response.refunded());
    }

    // Тест на порожній/null код купона відсутній — це і є прогалина в покритті.
}