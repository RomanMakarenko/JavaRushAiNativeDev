package com.example.catalog.products;

import java.util.List;

/**
 * Джерело даних для товарів каталогу.
 * Реалізація підключається через Spring; тут лише контракт вибірки.
 */
public interface ProductRepository {

    // Повертає всі товари з прапорцем visible = true
    List<Product> findAllVisible();
}