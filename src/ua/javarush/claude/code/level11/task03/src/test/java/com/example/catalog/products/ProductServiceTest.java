package com.example.catalog.products;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тести вибірки видимих товарів.
 * Перевіряють фільтрацію за категорією та порожній фільтр.
 */
class ProductServiceTest {

    private final ProductRepository repository = () -> List.of(
            new Product("p1", "Mug", "kitchen", new BigDecimal("9.90"), true),
            new Product("p2", "Notebook", "office", new BigDecimal("4.50"), true)
    );

    @Test
    void returnsAllVisibleWhenCategoryBlank() {
        ProductService service = new ProductService(repository);
        List<Product> result = service.findVisible(null);
        assertEquals(2, result.size());
    }

    @Test
    void filtersByCategory() {
        ProductService service = new ProductService(repository);
        List<Product> result = service.findVisible("office");
        assertEquals(1, result.size());
        assertTrue(result.get(0).name().equals("Notebook"));
    }
}