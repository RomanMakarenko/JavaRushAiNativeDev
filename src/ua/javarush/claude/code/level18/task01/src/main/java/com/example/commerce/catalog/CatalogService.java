package com.example.commerce.catalog;

import java.util.List;

/** Джерело товарів каталогу. У тесті підміняється мок-об’єктом. */
public interface CatalogService {

    List<Product> findProducts(int limit);
}