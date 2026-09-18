# Refund Demo Service

Невеликий capstone-сервіс, який за ідентифікатором замовлення збирає коротку
зведену інформацію про повернення коштів (refund summary): суму, статус і причину повернення.

Сервіс потрібен сапорт-агенту, щоб швидко відповісти клієнту, не відкриваючи
основну billing-панель.

## Запуск

```bash
./gradlew bootRun
```

## Demo endpoint

```bash
curl "http://localhost:8080/api/demo/refund-summary?orderId=A-1001"
```