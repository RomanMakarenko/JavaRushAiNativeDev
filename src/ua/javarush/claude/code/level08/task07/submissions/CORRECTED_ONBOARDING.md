# Onboarding (виправлено)

Ласкаво просимо до **example-shop** — Spring Boot застосунку з Vite + React фронтендом.  
Нижче — як підняти проект і з чого він складається.

## How to run

**Backend (Spring Boot + Gradle):**

```
./gradlew bootRun
```

**Frontend (Vite + React):**

```
cd apps/web
npm run start:dev
```

## Модулі

Сервіс складається з двох модулів. Сповіщення клієнтам **не реалізовано** — `RefundService` працює тільки з поверненнями коштів і не надсилає SMS.

| Клас | Роль |
|---|---|
| `OrderController` | `@RestController` на `/orders` — створення замовлення (`POST /orders`) та ініціація повернення (`POST /orders/refund`), яке делегується в `RefundService`. |
| `RefundService` | `@Service` — обробка повернення коштів за ідентифікатором замовлення. Повертає рядок `refunded:{orderId}`. |

## Assumptions

- **Фронтенд** запускається окремо від бекенда через Vite dev server.
- **Java 17** — цільова версія, вказана в `build.gradle.kts`.
- **Стек визначено з наявних файлів:** Spring Boot 3.2, Gradle Kotlin DSL, React 18, Vite 5, JUnit 5 (spring-boot-starter-test).

## Limitations

- **SMS-сповіщення відсутні.** У чернетці згадувався `SmsGateway`, але такого класу в коді немає. `RefundService` явно зазначає: "Жодних SMS-сповіщень тут немає".
- **База даних не підключена.** У залежностях (`build.gradle.kts`) присутні лише `spring-boot-starter-web` та `spring-boot-starter-test`.
- **Повернення коштів — заглушка.** `RefundService.processRefund()` повертає рядок `"refunded:" + orderId` без реальної інтеграції з платіжним сервісом.
- **Доступно лише два ендпоінти:** `POST /orders` (створення) та `POST /orders/refund` (повернення).
- **Безпека не налаштована.** У залежностях немає spring-boot-starter-security.