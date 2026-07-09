package com.example.catalog.products;

import java.math.BigDecimal;

/**
 * Доменна модель товару каталогу.
 */
public record Product(
        String id,
        String name,
        String category,
        BigDecimal price,
        boolean visible
) {
}