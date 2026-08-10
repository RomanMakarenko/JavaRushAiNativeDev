# Accepted review finding

- Severity: Blocker
- Status: accepted
- Area: `src/main/java/com/example/commerce/orders/OrderController.java`

## Зауваження
Контролер для порожнього кошика почав повертати `422 Unprocessable Entity`.
Схвалений план і наявні клієнти очікують `400 Bad Request`. Це порушення
backward compatibility.

## Прийняте рішення
Повернути статус `400` для порожнього кошика. Виправлення має бути локальним і
стосуватися лише поточного API path у `OrderController`. Тіло відповіді
`Cart is empty` залишається без змін.