# Затверджений план — значення за замовчуванням для query-параметрів

Контекст: у commerce-застосунку кілька REST-контролерів падають або поводяться непередбачувано, коли необов’язковий query-параметр не передано. Команда узгодила план із трьох незалежних кроків. Кожен крок — окрема керована ітерація: один крок, вузький список файлів, одна цільова перевірка.

## Крок 1 — orders: limit за замовчуванням

`GET /api/orders` має використовувати `limit=50`, якщо query-параметр `limit` не передано.

- Allowed files: `src/main/java/com/example/commerce/orders/OrderController.java`
- Targeted check: `./gradlew :orders:test --tests OrderControllerTest.usesDefaultLimitWhenMissing`

## Крок 2 — catalog: limit за замовчуванням

`GET /api/catalog` має використовувати `limit=20`, якщо query-параметр `limit` не передано.

- Allowed files: `src/main/java/com/example/commerce/catalog/CatalogController.java`
- Targeted check: `./gradlew :catalog:test --tests CatalogControllerTest.usesDefaultLimitWhenMissing`

## Крок 3 — inventory: warehouse за замовчуванням

`GET /api/inventory` має використовувати `warehouse="main"`, якщо параметр не передано.

- Allowed files: `src/main/java/com/example/commerce/inventory/InventoryController.java`
- Targeted check: `./gradlew :inventory:test --tests InventoryControllerTest.usesMainWarehouseByDefault`

## Правила ітерації

- Виконується рівно один крок за ітерацію, без забігання наперед.
- Змінюються лише файли зі списку Allowed files цього кроку.
- Після edits запускається лише targeted check цього кроку, а не весь suite.