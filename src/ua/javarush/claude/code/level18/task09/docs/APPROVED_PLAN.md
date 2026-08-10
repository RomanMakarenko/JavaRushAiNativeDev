# Approved plan — empty cart validation

## Goal
Під час спроби оформити замовлення з порожнім кошиком endpoint `POST /orders` має
повертати HTTP `400 Bad Request` із зрозумілим повідомленням про помилку.

## Scope
- Лише `OrderController` у модулі `orders`.
- Лише обробка порожнього кошика (cart без позицій).

## Non-goals
- Не чіпаємо розрахунок знижок і підсумкової суми.
- Не змінюємо інші endpoint'и модуля `orders`.
- Не додаємо нові залежності.

## Expected response
- Статус: `400`
- Тіло: повідомлення `Cart is empty`

## Verification
- Цільовий тест `OrderControllerTest.returnsBadRequestForEmptyCart` має бути зеленим.