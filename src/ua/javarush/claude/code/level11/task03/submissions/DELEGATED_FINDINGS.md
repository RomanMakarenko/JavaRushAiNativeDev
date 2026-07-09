# DELEGATED FINDINGS: GET /api/products

**Agent:** Explore (`a68e6477389307701`)
**Date:** 2026-07-09
**Directory:** `.../level11/task03`
**Verified:** Findings confirmed by direct code reading of all source files
**Test file:** `src/test/java/com/example/catalog/products/ProductServiceTest.java` — присутній, 2 тести

## Trace

No trace/delegated worker labels found in the codebase. Standard Spring MVC `@RestController` with a single `@GetMapping` endpoint.

## Findings

### 1. Controller — точка входу
- **Файл:** `src/main/java/com/example/catalog/products/ProductController.java`
- **Рядок:** 23-28
- **Що робить:** `@GetMapping("/api/products")` приймає опціональний параметр `category`, викликає `productService.findVisible(category)`, мапить `Product` у `ProductResponse` через `ProductResponse::from`, повертає `List<ProductResponse>`.
- **Тип:** controller

### 2. Service — бізнес-логіка/фільтрація
- **Файл:** `src/main/java/com/example/catalog/products/ProductService.java`
- **Рядок:** 21-28
- **Що робить:** `findVisible(String category)` отримує всі видимі товари з `ProductRepository.findAllVisible()`. Якщо категорія null або пуста — повертає всі. Інакше фільтрує за case-insensitive збігом категорії.
- **Тип:** service

### 3. Repository — контракт даних
- **Файл:** `src/main/java/com/example/catalog/products/ProductRepository.java`
- **Рядок:** 12
- **Що робить:** Інтерфейс з одним методом `List<Product> findAllVisible()`. Реалізація підключається через Spring (не в цій директорії).
- **Тип:** repository (interface)

### 4. Product — доменна модель
- **Файл:** `src/main/java/com/example/catalog/products/Product.java`
- **Рядок:** 8-14
- **Що робить:** Java `record` з полями: `id` (String), `name` (String), `category` (String), `price` (BigDecimal), `visible` (boolean). Поле `visible` присутнє тут, але приховується під час конвертації в DTO.
- **Тип:** model

### 5. ProductResponse — DTO відповіді (фактична JSON-структура)
- **Файл:** `src/main/java/com/example/catalog/products/ProductResponse.java`
- **Рядок:** 9-23 (record: 9-14; factory метод: 16-22)
- **Що робить:** Java `record` з полями: `id`, `name`, `category`, `price` (BigDecimal). Статичний фабричний метод `from(Product)` (рядок 16-22) копіює ці 4 поля, **явно виключаючи** `visible`. Саме цей тип Jackson серіалізує в JSON — він визначає точну JSON-структуру відповіді.
- **Тип:** DTO (model)

### 6. ProductServiceTest — тести сервісного шару
- **Файл:** `src/test/java/com/example/catalog/products/ProductServiceTest.java`
- **Рядок:** 17-35
- **Що робить:** Два тести: `returnsAllVisibleWhenCategoryBlank` (рядок 23) перевіряє, що при null-категорії повертаються всі видимі товари; `filtersByCategory` (рядок 30) перевіряє фільтрацію за назвою категорії. Репозиторій — in-memory лямбда (рядок 17) з двома продуктами.
- **Тип:** test

### 7. @RestController — неявна JSON-серіалізація
- **Файл:** `src/main/java/com/example/catalog/products/ProductController.java`
- **Рядок:** 13 (`@RestController`)
- **Що робить:** `@RestController` = `@Controller` + `@ResponseBody`. Spring Boot автоматично серіалізує `List<ProductResponse>` у JSON через Jackson `MappingJackson2HttpMessageConverter`. Жодного явного `ObjectMapper` у коді немає.
- **Тип:** config (annotation-driven)

## Ланцюжок формування відповіді (4 шари)

```
ProductRepository.findAllVisible()    [репозиторій — джерело даних]
        ↓
ProductService.findVisible(category)  [сервіс — фільтрація]
        ↓
ProductController.listProducts()      [контролер — @GetMapping]
        ↓
ProductResponse.from(product)         [DTO — формування JSON]
        ↓
@RestController / Jackson             [неявна серіалізація в JSON]
```

## Open questions

### Реалізація ProductRepository
- Інтерфейс `ProductRepository.findAllVisible()` (рядок 12) повертає `List<Product>`. Конкретна реалізація **відсутня в цій директорії**. Ймовірно, знаходиться в батьківському модулі або інжектується під час виконання. Не можна визначити, чи повертає `findAllVisible()` in-memory список, БД-запит тощо. Також не підтверджено, чи може метод повернути `null` (що спричинило б NPE в `ProductService` рядок 22).

### Конфігураційні файли
- `find` за `*.properties`, `*.xml`, `*.yml`, `*.yaml` у директорії task03 не знайшов жодного файлу. `application.properties` з `server.servlet.context-path` або подібними налаштуваннями маршрутизації не знайдено. Можливо, знаходиться в батьківському модулі.

### Spring Boot Application клас
- Жодного `*Application.java` з `@SpringBootApplication` не знайдено в task03 або на 4 рівні вгору. Ймовірно, в батьківському модулі за межами області пошуку.