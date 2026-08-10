# Затверджений план — значення за замовчуванням для параметрів запиту

Контекст: у commerce-застосунку кілька REST-контролерів падають або поводяться
непередбачувано, коли необов’язковий параметр запиту не передано. Команда узгодила
план із трьох незалежних кроків. Кожен крок — окрема контрольована ітерація: один крок,
вузький список файлів, одна цільова перевірка.

## Крок 1 — orders: limit за замовчуванням

`GET /api/orders` має використовувати `limit=50`, якщо параметр запиту `limit` не передано.

- Дозволені файли: `src/main/java/com/example/commerce/orders/OrderController.java`
- Цільова перевірка: `./gradlew :orders:test --tests OrderControllerTest.usesDefaultLimitWhenMissing`

## Крок 2 — catalog: limit за замовчуванням

`GET /api/catalog` має використовувати `limit=20`, якщо параметр запиту `limit` не передано.

- Дозволені файли: `src/main/java/com/example/commerce/catalog/CatalogController.java`
- Цільова перевірка: `./gradlew :catalog:test --tests CatalogControllerTest.usesDefaultLimitWhenMissing`

## Крок 3 — inventory: warehouse за замовчуванням

`GET /api/inventory` має використовувати `warehouse="main"`, якщо параметр не передано.

- Дозволені файли: `src/main/java/com/example/commerce/inventory/InventoryController.java`
- Цільова перевірка: `./gradlew :inventory:test --tests InventoryControllerTest.usesMainWarehouseByDefault`

## Правила ітерації

- Виконується рівно один крок за ітерацію, без випередження.
- Змінюються лише файли зі списку Allowed files цього кроку.
- Після змін запускається лише цільова перевірка цього кроку, а не весь набір тестів.