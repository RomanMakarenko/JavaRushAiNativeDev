package com.example.shop.refund;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Сортування refund-запитів в inbox оператора. */
public class RefundInboxService {

    /** Повертає запити, відсортовані за часом створення (старіші зверху). */
    public List<RefundRequest> sortByOldestFirst(List<RefundRequest> requests) {
        List<RefundRequest> sorted = new ArrayList<>(requests);
        sorted.sort(Comparator.comparing(RefundRequest::getCreatedAt));
        return sorted;
    }
}