# Контекст проєкту

Gradle-проєкт `orders` — сервісний шар оформлення замовлень інтернет-магазину. Ми ведемо серію
маленьких behavior-preserving рефакторингів за принципом safe incremental loop: один маленький
крок → цільова перевірка → фіксація. Жодного широкого cleanup і жодних змін public API
за один крок.

## Ключові класи

- `src/main/java/com/example/orders/OrderService.java` — розрахунок підсумку замовлення і валідація позицій.
- `src/main/java/com/example/orders/DiscountCalculator.java` — розрахунок знижки за сумою замовлення.

## Тести

- `src/test/java/com/example/orders/OrderServiceTest.java`
- `src/test/java/com/example/orders/DiscountCalculatorTest.java`

## Команди

- Усі тести: `./gradlew test`
- Цільовий тест сервісу замовлень: `./gradlew test --tests '*OrderServiceTest'`
- Цільовий тест калькулятора знижки: `./gradlew test --tests '*DiscountCalculatorTest'`
- Статус робочого дерева: `git status --short`

## Правила refactor-step

- Один крок зачіпає рівно один production-файл.
- Public API (public-методи і public-поля) в межах кроку не змінюється.
- Test-файли в кроці, що зберігає поведінку, не редагуються.
- Перед кроком фіксується baseline, після кроку запускається цільова перевірка.