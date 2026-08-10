# Empty cart returns 400 on POST /orders

## Summary
Замовлення з порожнім кошиком тепер відхиляється на вході контролера із зрозумілим
повідомленням про помилку замість падіння далі по потоку.

## Changes
- `OrderController.createOrder` перевіряє, що кошик не порожній, і повертає помилку.

## Verification
- `./gradlew :orders:test --tests OrderControllerTest.returnsBadRequestForEmptyCart`

## Risk
- Зміна локальна, зачіпає лише один шлях обробки порожнього кошика.