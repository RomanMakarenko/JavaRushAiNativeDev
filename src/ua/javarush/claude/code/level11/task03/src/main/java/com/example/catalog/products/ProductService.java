package com.example.catalog.products;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Бізнес-логіка вибірки товарів.
 * Повертає лише видимі товари, за потреби фільтруючи за категорією.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Відбирає видимі товари; порожня категорія означає «всі категорії»
    public List<Product> findVisible(String category) {
        List<Product> all = productRepository.findAllVisible();
        if (category == null || category.isBlank()) {
            return all;
        }
        return all.stream()
                .filter(p -> category.equalsIgnoreCase(p.category()))
                .toList();
    }
}