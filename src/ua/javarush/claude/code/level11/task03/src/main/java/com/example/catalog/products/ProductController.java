package com.example.catalog.products;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контролер каталогу товарів Commerce OS.
 * Обробляє GET /api/products і делегує вибірку в ProductService.
 */
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Точка входу для GET /api/products: приймає фільтр категорії та формує відповідь
    @GetMapping("/api/products")
    public List<ProductResponse> listProducts(@RequestParam(required = false) String category) {
        List<Product> products = productService.findVisible(category);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
}