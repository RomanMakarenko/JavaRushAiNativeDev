# Issue #482

Замовлення не оформлюється, якщо кошик порожній.
На фронті показується загальна помилка.

Очікуємо зрозуміле повідомлення для користувача.
Напевно, треба додати перевірку в `OrderController`.

Додано stack trace:

```
java.lang.ArithmeticException: / by zero
    at com.rush.commerce.order.OrderService.calculateTotal(OrderService.java:88)
    at com.rush.commerce.order.OrderController.createOrder(OrderController.java:54)
```

Відтворюється при `POST /api/orders` з порожнім `items[]`.