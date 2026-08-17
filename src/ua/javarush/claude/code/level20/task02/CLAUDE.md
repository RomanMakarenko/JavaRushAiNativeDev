# CLAUDE.md

Цей файл допомагає Claude Code орієнтуватися в проєкті `orders-service`.

## Що це за проєкт

`orders-service` — backend на Spring Boot (Gradle, Kotlin DSL) плюс невеликий
frontend у каталозі `web/` (Node.js, npm). Backend відповідає за оформлення
замовлень, frontend — це адмінська панель для перегляду замовлень.

## Структура

- `src/main/java/com/example/orders` — backend-код сервісу замовлень
- `src/test/java/com/example/orders` — backend-тести
- `web/` — frontend-застосунок (npm scripts у `web/package.json`)
- `build.gradle.kts` — конфігурація збірки backend, плагіни та завдання

## Збірка і перевірки

Backend збирається через Gradle Wrapper (`./gradlew`), форматування
забезпечує плагін Spotless. Frontend використовує npm scripts із `web/package.json`.
Конкретні команди дивись у `build.gradle.kts` і `web/package.json`.

## Local verification harness

Для локальної перевірки використовуйте лише наявні команди проєкту — швидкі сенсори: `./gradlew spotlessCheck` (format) та `npm run lint`; повні сенсори: `./gradlew build` + `./gradlew test` та `npm run build` + `npm run test` — і завжди покладайтеся на наявні scripts і patterns проєкту з поточним toolchain (Gradle Wrapper, npm), не замінюючи їх.