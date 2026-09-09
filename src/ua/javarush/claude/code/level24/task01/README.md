# Commerce OS

Сервіс електронної комерції: каталог, замовлення, повернення (refund flow).

## Запуск

```
./gradlew run
```

Перед production-релізом будь-який PR проходить через production decision gate команди.

## Аудит повернень

Дії за refund-політикою пишуться в `refund_policy_audit` (міграція V104).
