package com.acme.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderExportServiceTest {

    @Test
    void buildsCsvWithHeaderAndRows() {
        OrderExportService service = new OrderExportService();
        String csv = service.toCsv(List.of(new Order(1L, 100L), new Order(2L, 250L)));
        assertEquals("id,total\n1,100\n2,250\n", csv);
    }
}
