# Orienting response — перший огляд вітрини Commerce OS

> Сесію Claude Code розпочато з `/clear`, щоб отримати чистий контекст без
> слідів попередніх обговорень. Мета — лише орієнтація, без правок коду.

## Languages / frameworks
- Java + Spring Boot 4.0.6 — backend (джерело: `build.gradle.kts`, плагін `org.springframework.boot`)
- Next.js 16.2 / React 19.2 — frontend (джерело: `frontend/package.json`, залежність `next`)
- PostgreSQL 18.3 — база даних (джерело: `docker-compose.yml`, сервіс `db`)

## Top-level directories
- `src/` — backend-код і тести на Java (`src/main/java/com/acme/store`)
- `frontend/` — frontend-застосунок на Next.js (`frontend/package.json`)
- `submissions/` — робочі discovery-нотатки
- `docker-compose.yml` — локальна інфраструктура (db + backend)

## Run / test commands
- Запуск backend: `./gradlew bootRun` (джерело: Spring Boot плагін у `build.gradle.kts`)
- Тести backend: `./gradlew test` (джерело: `tasks.withType<Test>` у `build.gradle.kts`)
- Запуск frontend: `npm run dev` (джерело: script `dev` у `frontend/package.json`)
- Тести frontend: `npm test` (джерело: script `test` → `vitest run`)
- Локальна інфраструктура: `docker compose up` (джерело: `docker-compose.yml`)

## Entry points
- Старт backend: `src/main/java/com/acme/store/StoreApplication.java` (клас з `@SpringBootApplication`)
- HTTP-домен orders: наявність `src/test/java/com/acme/store/orders/OrderControllerTest.java` вказує на модуль orders з HTTP-маршрутами
- Старт frontend: `frontend/package.json`, script `dev` (`next dev`)

## Open questions
- Де розташований сам `OrderController` — у цьому зрізі видно лише його тест, а вихідний код контролера не знайдено?
- Чи є єдина команда для одночасного запуску backend і frontend, чи їх підіймають окремо?
- Які ще доменні модулі (catalog, payments) присутні в проєкті — наразі підтверджено лише `orders`?