package com.example.shop.checkout;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест контролера оформлення замовлення.
 * Перевіряє, що запит з порожнім email повертає 400, а не 500.
 */
class CheckoutControllerTest {

    private final CheckoutController controller =
            new CheckoutController(new CheckoutService());

    @Test
    void createOrder_validationFails() {
        // Порожній email має приводити до статусу 400 Bad Request.
        CheckoutRequest request = new CheckoutRequest(null, "SKU-42", 1);

        int status = controller.checkout(request).getStatusCode().value();

        assertEquals(400, status);
    }
}