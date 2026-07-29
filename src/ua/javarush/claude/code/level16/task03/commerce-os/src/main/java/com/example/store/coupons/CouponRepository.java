package com.example.store.coupons;

import java.util.List;

/** Доступ до активних купонів. */
public interface CouponRepository {

    /** Повертає всі активні купони. */
    List<Coupon> findActive();
}
