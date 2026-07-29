package com.example.store.coupons;

import java.math.BigDecimal;

/** Джерело курсів конвертації валют. */
public interface RateProvider {

    /** Курс переведення з валюти from у валюту to. */
    BigDecimal rate(String from, String to);
}
