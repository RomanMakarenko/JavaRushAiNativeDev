package com.example.orders;

import org.springframework.stereotype.Service;

@Service
public class OrderLabelService {

    // Нормалізує label замовлення перед збереженням.
    public String normalizeLabel(String label) {
        if (label == null) {
            return null;
        }
        return label;
    }
}