package com.example.commerce.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<Product> list(@RequestParam(required = false) Integer limit) {
        // За замовчуванням, якщо limit не передано, використовуємо 20.
        if (limit == null) {
            limit = 20;
        }
        return catalogService.findProducts(limit);
    }
}