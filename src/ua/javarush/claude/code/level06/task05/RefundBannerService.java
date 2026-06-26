package com.example.commerce.refund;

/**
 * Формує текст банера для сторінки повернення.
 * Це потрібний edit гілки refund-banner-fix — його чіпати НЕ потрібно.
 */
public class RefundBannerService {

    /** Повертає текст банера за сумою повернення. */
    public String bannerFor(int refundAmount) {
        if (refundAmount <= 0) {
            // потрібна правка: при нульовому поверненні банер не показуємо
            return "";
        }
        return "Повернення на суму " + refundAmount + " руб. оформлено";
    }
}