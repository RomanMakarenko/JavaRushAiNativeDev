# Orienting response — перший огляд вітрини Commerce OS

> Сесію Claude Code розпочато з `/clear`, щоб отримати чистий контекст без
> слідів попередніх обговорень. Мета — лише орієнтація, без правок коду.

## Languages / frameworks

| Компонент | Технологія | Джерело |
|---|---|---|
| Backend | **Java 25 + Spring Boot 4.0.6** (Gradle) | `build.gradle.kts`: `sourceCompatibility = JavaVersion.VERSION_25`, плагін `org.springframework.boot` версії `4.0.6`, `group = "com.acme.store"` |
| Frontend | **Next.js 16.2 / React 19.2** (npm) | `frontend/package.json`: залежності `next@16.2.0`, `react@19.2.0`, `react-dom@19.2.0` |
| База даних | **PostgreSQL 18.3** | `docker-compose.yml`: сервіс `db` з образом `postgres:18.3` |
| Тести backend | JUnit 5 (JUnit Platform) | `build.gradle.kts`: `useJUnitPlatform()` |
| Тести frontend | Vitest 2.1.0 | `frontend/package.json`: dev-залежність `vitest@2.1.0` |
| Збірка backend | Gradle | `build.gradle.kts` |
| Збірка frontend | npm | `frontend/package.json`: скрипти `build`, `dev`, `start` |

## Top-level directories

```
task-workspace/
├── build.gradle.kts       # Збірка backend: Java, Spring Boot, залежності, тести
├── docker-compose.yml     # Локальна інфраструктура: PostgreSQL + backend-контейнер
├── frontend/              # Frontend-застосунок (тільки package.json, коду немає)
│   └── package.json
├── submissions/           # Discovery-нотатки та артефакти дослідження
│   └── ORIENTING_RESPONSE.md
└── src/
    ├── main/
    │   └── java/com/acme/store/
    │       └── StoreApplication.java   # Точка входу backend
    └── test/
        └── java/com/acme/store/orders/
            └── OrderControllerTest.java  # Тест-заглушка для модуля orders
```

- `src/main/` — backend-код (Java, пакет `com.acme.store`)
- `src/test/` — тести backend (Java, пакет `com.acme.store.*`)
- `frontend/` — Next.js frontend (наразі тільки `package.json`)
- `submissions/` — нотатки дослідження
- Проєктна версія: `0.1.0-SNAPSHOT` (`build.gradle.kts`: `version = "0.1.0-SNAPSHOT"`)

## Run / test commands

### Backend
| Команда | Дія | Джерело |
|---|---|---|
| `./gradlew bootRun` | Запуск backend на порту `:8080` | Spring Boot плагін у `build.gradle.kts` |
| `./gradlew test` | Запуск тестів (JUnit 5) | `build.gradle.kts`: `tasks.withType<Test> { useJUnitPlatform() }` |
| `./gradlew build` | Повна збірка | Стандартний Gradle task |

### Frontend
| Команда | Дія | Джерело |
|---|---|---|
| `npm run dev` | Режим розробки (`next dev`) | `frontend/package.json`, script `dev` |
| `npm run build` | Продакшен-збірка | `frontend/package.json`, script `build` |
| `npm start` | Запуск продакшен-збірки | `frontend/package.json`, script `start` |
| `npm test` | Тести через Vitest | `frontend/package.json`, script `test` → `vitest run` |

### Інфраструктура
| Команда | Дія | Джерело |
|---|---|---|
| `docker compose up` | Підіймає PostgreSQL + backend | `docker-compose.yml` |

## Entry points

1. **Backend-застосунок** — `src/main/java/com/acme/store/StoreApplication.java`, метод `main()`:
   - Анотація `@SpringBootApplication`
   - Виклик `SpringApplication.run(StoreApplication.class, args)`
   - Пакет: `com.acme.store`

2. **Домен orders (HTTP)** — наразі відомий тільки через тест:
   - `src/test/java/com/acme/store/orders/OrderControllerTest.java` — клас з `@SpringBootTest`
   - Містить метод `placeOrderReturnsCreated()` з анотацією `@Test` (тіло методу порожнє — це заглушка)
   - Пакет `com.acme.store.orders` вказує на очікуваний контролер `OrderController`, який **ще не створено**

3. **Frontend** — `frontend/package.json`, script `dev` → `next dev`
   - Точка входу визначається Next.js (фреймворк сам знаходить сторінки)

## Open questions

1. **Де `OrderController`?** — Тест `OrderControllerTest.java` існує в пакеті `com.acme.store.orders`, але сам контролер не знайдено. Його потрібно створити.
2. **Де Dockerfile?** — `docker-compose.yml` має `backend: build: .`, що вказує на Dockerfile в корені, але файл відсутній.
3. **Де Spring-конфігурація?** — Немає `application.yml` або `application.properties`. Spring Boot працюватиме з дефолтними налаштуваннями (порт `:8080`, без datasource).
4. **Де `settings.gradle.kts`?** — Відсутній у цій директорії. Незрозуміло, чи проєкт використовує Gradle Wrapper з кореня всього `javarush-project`, чи розраховує на глобально встановлений Gradle.
5. **Яка архітектура frontend?** — У `frontend/` є тільки `package.json`. Немає жодних сторінок, компонентів, `next.config.js`, `tsconfig.json` чи стилів.
6. **Які ще доменні модулі будуть?** — Наразі підтверджено лише `orders`. Очікувані домени (catalog, payments, users, cart) відсутні.
7. **Чи є спільна команда запуску?** — Backend і frontend запускаються окремо. Немає `docker-compose` профілю чи скрипта для одночасного підйому всього стеку.