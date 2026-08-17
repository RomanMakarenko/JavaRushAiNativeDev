# Запропоновані зміни в сервісі замовлень

Команда накидала список конкретних правок до коду в `src/main/java/com/example/orders`.
Кожну правку потрібно класифікувати **до будь-яких edits**: чи є вона
behavior-preserving refactor (форма змінюється, зовнішньо спостережувана поведінка —
ні) чи вона змінює externally visible behavior і, отже, refactor-ом уже не є.

Усі пункти стосуються наявного коду `OrderService`, `OrderController`,
`OrderResponse`. Нічого змінювати не потрібно — лише рознести за цією межею.

1. Перейменувати private-метод `processOrderChecks` в `OrderService` на
   `validateOrderRequest`, оновивши внутрішній виклик у `placeOrder`. Тіло методу
   не чіпати.

2. У `processOrderChecks` поміняти місцями перевірки: спочатку перевіряти
   `amount`, потім `orderId`. Самі умови й тексти повідомлень залишити без змін.

3. У `OrderService.cancelOrder` замінити рядковий статус `"CANCELLED"`,
   який кладеться в `OrderResponse`, на `"CANCELED"` (одна `L`), щоб
   відповідати американському написанню.

4. У `findOrder` ввести локальну змінну `notFoundMessage` для тексту
   `"Order not found: " + orderId` і використовувати її у винятку. Сам текст
   і тип винятку залишити без змін.

5. Перейменувати public-метод `findOrder` в `OrderService` на `getOrder` і
   оновити виклик в `OrderController.getOrder`. Сигнатуру (параметри, тип, що
   повертається) зберегти.