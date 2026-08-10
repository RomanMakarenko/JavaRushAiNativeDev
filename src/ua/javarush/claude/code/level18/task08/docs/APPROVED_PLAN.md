# Approved plan: issue-432 empty cart

Узгоджений план змін (після plan-mode investigation):

1. У `OrderController.createOrder` додати ранню перевірку кошика: якщо `items`
   порожній або `null`, повернути `400 Bad Request` із тілом `Cart is empty` до будь-якого
   обчислення суми.
2. Не змінювати поведінку для непорожнього кошика (`200 OK`, тіло `Order accepted`).
3. Покрити виправлення цільовим тестом
   `OrderControllerTest.returnsBadRequestForEmptyCart`.

## Свідомо поза scope
- Валідація від’ємного або нульового `quantity` — окреме issue.
- Зміни схеми API та контракту успішної відповіді.
- Рефакторинг обчислення суми замовлення.