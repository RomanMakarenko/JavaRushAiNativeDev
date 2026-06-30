# DEPENDENCY FINDING: Відсутня проєктна залежність `:analytics` → `:support-api`

## Тип
Розбіжність карти модулів і фактичної збірки — відсутня залежність.

## Модульна карта (settings.gradle.kts)
Обидва модулі включені в проєкт, отже зв'язок між ними передбачений:
```
include(":analytics")
include(":support-api")
```

## Фактична залежність у коді
`AnalyticsController` (модуль `:analytics`) імпортує та використовує публічний тип із модуля `:support-api`:

**Файл:** `analytics/src/main/java/com/acme/analytics/AnalyticsController.java`
```java
import com.acme.support.api.TicketClient;
```
- Рядок 3 — імпорт класу `TicketClient`.
- Рядок 8 — поле `private final TicketClient ticketClient;`
- Рядок 16 — виклик `ticketClient.countOpenTickets();`

**Файл:** `support-api/src/main/java/com/acme/support/api/TicketClient.java`
Пакет `com.acme.support.api` — клас оголошено в модулі `:support-api`.

## Відсутність у збірці
**Файл:** `analytics/build.gradle.kts`
Блок `dependencies {}` містить лише:
```kotlin
testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
```
**Відсутня:** `implementation(project(":support-api"))` або еквівалентна проєктна залежність.

## Наслідок
Компіляція модуля `:analytics` падає з помилкою "cannot find symbol `TicketClient`", оскільки Gradle не знає про зв'язок між модулями й не додає `support-api` до classpath під час збірки `analytics`.

## Рекомендована правка
Додати в `analytics/build.gradle.kts` у блок `dependencies`:
```kotlin
implementation(project(":support-api"))
```