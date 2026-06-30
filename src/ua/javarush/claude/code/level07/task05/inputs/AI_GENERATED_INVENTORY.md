# CODEBASE_INVENTORY — store (чернетка, згенерована асистентом)

> УВАГА: чернетка від Claude Code, не звірена з реальним деревом. Містить помилки.

## Languages / frameworks
- Java 17 + Spring Boot — `build.gradle.kts`.
- React + Vite — `frontend/package.json`.

## Modules and responsibilities
- `com.acme.store` — корінь backend.
- `recommendations/` — модуль рекомендацій товарів на основі історії покупок.
- `frontend` — вітрина магазину.

## Entry points
- Backend: `StoreApplication.main`.

## Tests
- Backend-тести через `./gradlew test`.

## Configs
- `build.gradle.kts`, `settings.gradle.kts`.

## Useful commands
- `./gradlew test`
- `npm --prefix frontend test`

## Open questions
- Як налаштовується підключення до бази даних?