package com.example.store.coupons;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/** Бізнес-логіка вибірки та перерахунку купонів під валюту замовлення. */
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CurrencyConverter currencyConverter;

    public CouponService(CouponRepository couponRepository, CurrencyConverter currencyConverter) {
        this.couponRepository = couponRepository;
        this.currencyConverter = currencyConverter;
    }

    /** Активні купони; для multi-currency сума знижки конвертується у валюту замовлення. */
    public List<CouponView> findActiveCoupons(String currency) {
        return couponRepository.findActive().stream()
                .map(coupon -> toView(coupon, currency))
                .collect(Collectors.toList());
    }

    private CouponView toView(Coupon coupon, String currency) {
        BigDecimal amount = coupon.getDiscountAmount();
        if (currency != null && !currency.equals(coupon.getBaseCurrency())) {
            // конвертація суми знижки у валюту замовлення
            amount = currencyConverter.convert(amount, coupon.getBaseCurrency(), currency);
        }
        return new CouponView(coupon.getCode(), amount, currency);
    }
}
