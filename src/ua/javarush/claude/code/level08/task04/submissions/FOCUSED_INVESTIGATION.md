# FOCUSED INVESTIGATION

## Code focus

- `CouponController.java:21–26`
- `CouponService.java:12`

## Runtime evidence

`inputs/error-snippet.txt` — запит `POST /api/coupons/validate` повертає `500 Internal Server Error`:

```
java.lang.NullPointerException: Cannot invoke "String.toUpperCase()"
because "code" is null
    at com.example.shop.coupons.CouponService.validate(CouponService.java:12)
    at com.example.shop.coupons.CouponController.validate(CouponController.java:21)
```

## Likely cause

`request.code()` повертає `null`, і `.toUpperCase()` на `CouponService.java:12` кидає NPE.

## Next check

Перевірити `CouponRequest`, чи поле `code` допускає `null`.