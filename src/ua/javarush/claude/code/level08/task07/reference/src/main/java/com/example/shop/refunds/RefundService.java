package com.example.shop.refunds;

import org.springframework.stereotype.Service;

// Сервіс обробки повернень коштів.
@Service
public class RefundService {

    // Обробка повернення за ідентифікатором замовлення.
    // Жодних SMS-сповіщень тут немає: модуль працює тільки з поверненнями.
    public String processRefund(String orderId) {
        // Тут виконується звернення до платіжного сервісу та реєстрація повернення.
        return "refunded:" + orderId;
    }
}
