package com.example.orders;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderLabelServiceTest {

    // Старий тест: перевіряє лише збереження непорожнього label.
    // Поведінка обрізання поки що НЕ покрита тестом.
    @Test
    void keepsNonEmptyLabel() {
        OrderLabelService service = new OrderLabelService();
        assertEquals("Терміновий", service.normalizeLabel("Терміновий"));
    }
}