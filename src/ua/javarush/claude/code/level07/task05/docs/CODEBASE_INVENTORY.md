# CODEBASE_INVENTORY — store

> Складено за реальним деревом репозиторію. Джерела вказано для критичних зауважень.

## Modules

| Модуль | Технології | Джерело |
|---|---|---|
| **Backend** — `src/main/java/com/acme/store/` | Java 17, Spring Boot 3.2.5, Gradle (Kotlin DSL) | [`build.gradle.kts`](../build.gradle.kts) |
| **Frontend** — `frontend/` | React 18, Vite 5, Vitest | [`frontend/package.json`](../frontend/package.json) |

### Entry points

- Backend: `com.acme.store.StoreApplication.main` — [`src/main/java/com/acme/store/StoreApplication.java`](../src/main/java/com/acme/store/StoreApplication.java)

### Dependencies (backend)

- `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `postgresql` (runtime), `spring-boot-starter-test` (test)

### Dependencies (frontend)

- `react`, `react-dom` (runtime); `vite`, `vitest` (dev)

---

## High-risk areas

| Ризик | Обґрунтування |
|---|---|
| **Немає application.properties / application.yml** | `spring-boot-starter-web` + `data-jpa` + `postgresql` залежності додані, але конфігурація підключення до БД, порту тощо відсутня. Додаток не запуститься без `src/main/resources/application.properties`. Джерело: [`build.gradle.kts`](../build.gradle.kts):21–22 |
| **Відсутній Gradle Wrapper** | `./gradlew` не працює — у репозиторії немає `gradlew`, `gradlew.bat`, ані `gradle/wrapper/`. Потрібен `gradle wrapper`. Джерело: реальне дерево файлів |
| **Немає тестів** | `spring-boot-starter-test` оголошено, але директорія `src/test/` відсутня. На фронтенді — лише `package.json` без жодного `.spec`/`.test` файлу. Джерело: реальне дерево файлів |
| **Порожній docs/CODEBASE_INVENTORY.md** | Файл існує, але пустий. Поточний документ — спроба виправити це. Джерело: [`docs/CODEBASE_INVENTORY.md`](./CODEBASE_INVENTORY.md) |

---

## Open questions

1. **Налаштування бази даних.** Як підключатися до PostgreSQL? Відсутній `application.properties` — незрозуміло очікуваний `datasource.url`, `username`, `password`, чи це H2 для dev-режиму. Джерело: [`build.gradle.kts`](../build.gradle.kts):22 — `runtimeOnly("org.postgresql:postgresql")`

2. **Структура фронтенду.** Наявний лише `package.json` — чи планується додавати `frontend/src/` із компонентами React? Чи це ініціалізований, але незаповнений модуль?

3. **Спосіб запуску без Gradle Wrapper.** Як зараз запускається проект — через встановлений системний Gradle, через IDE, чи wrapper буде додано пізніше?

4. **Відсутність `src/main/resources/`.** Як проєкт обробляє конфігурацію Spring Boot без стандартної директорії ресурсів?