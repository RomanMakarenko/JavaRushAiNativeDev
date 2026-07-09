package com.example.catalog.products;

import java.math.BigDecimal;

/**
 * DTO відповіді для GET /api/products.
 * Будується з доменної моделі Product і приховує службові поля.
 */
public record ProductResponse(
        String id,
        String name,
        String category,
        BigDecimal price
) {
    // Перетворює доменний Product у публічний response
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.category(),
                product.price()
        );
    }
}