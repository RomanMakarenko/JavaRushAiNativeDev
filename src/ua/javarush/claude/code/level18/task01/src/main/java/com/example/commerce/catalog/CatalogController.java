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

    // Поточний стан до кроку: limit обов’язковий, значення за замовчуванням відсутнє.
    @GetMapping
    public List<Product> list(@RequestParam Integer limit) {
        return catalogService.findProducts(limit);
    }
}