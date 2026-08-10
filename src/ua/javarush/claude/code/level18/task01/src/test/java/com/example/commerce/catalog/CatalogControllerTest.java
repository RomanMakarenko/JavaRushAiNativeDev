package com.example.commerce.catalog;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatalogControllerTest {

    @Test
    void usesDefaultLimitWhenMissing() {
        CatalogService service = mock(CatalogService.class);
        when(service.findProducts(eq(20))).thenReturn(List.of(new Product("sku-1")));

        CatalogController controller = new CatalogController(service);

        // limit не передано -> очікуємо значення за замовчуванням 20
        List<Product> result = controller.list(null);

        assertThat(result).hasSize(1);
    }
}