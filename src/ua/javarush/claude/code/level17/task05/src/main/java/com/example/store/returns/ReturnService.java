package com.example.store.returns;

import org.springframework.stereotype.Service;

@Service
public class ReturnService {

    // Бізнес-логіка повернення купона.
    public ReturnResponse processReturn(String couponCode) {
        // УВАГА: за порожнього коду тут кидається NullPointerException,
        // який не обробляється в обробнику помилок і доходить до клієнта як 500.
        if (couponCode.trim().isEmpty()) {
            throw new IllegalStateException("Купон уже повернуто");
        }
        return new ReturnResponse(couponCode, true);
    }
}