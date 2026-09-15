# Current state

## Current versions

| Component | Version / value | Source |
| --- | --- | --- |
| Spring Boot | `2.7.18` | `build.gradle` |
| Spring Dependency Management plugin | `1.0.15.RELEASE` | `build.gradle` |
| Application | `0.9.3` | `build.gradle` |
| Gradle Wrapper | `7.6.4` | `gradle/wrapper/gradle-wrapper.properties` |
| Java in CI | `8` (Temurin) | `.github/workflows/ci.yml` |
| PostgreSQL container | `12` | `docker-compose.yml` |

## Build/runtime tools

- Build system: Gradle через Gradle Wrapper; репозиторій залежностей — Maven Central.
- Gradle-плагіни: `java`, Spring Boot, Spring Dependency Management.
- Spring Boot starters: Web, Security та Data JPA.
- Runtime dependency: PostgreSQL JDBC driver (`org.postgresql:postgresql`).
- Test dependency: Spring Boot Test.
- Локальна база даних описана в `docker-compose.yml`: PostgreSQL на порту `5432`, база `cashflow`, користувач `cashflow`.
- CI використовує Temurin JDK 8 на `ubuntu-latest`.

## CI command

Baseline-команда CI для перевірки проєкту:

```bash
./gradlew clean test
```

Вона запускається у workflow для push у `main` та для pull request.

## Deprecated APIs

- `src/main/java/com/acme/cashflow/security/SecurityConfig.java:7` використовує `WebSecurityConfigurerAdapter`, який є deprecated у Spring Security 5.7+.
- `src/main/java/com/acme/cashflow/security/SecurityConfig.java:14-15` використовує ланцюжок `authorizeRequests().antMatchers(...)` — ці API позначені як deprecated у Spring Security 5.8 та замінюються сучасним DSL.

Це зафіксовані сигнали з Java-коду; документ не пропонує виконувати міграцію чи визначає цільову версію.

## Unknowns

- Не підтверджено, що `./gradlew` (wrapper launcher script) присутній саме в цій робочій директорії; CI посилається на нього, але в доступному переліку файлів є лише `gradle/wrapper/gradle-wrapper.properties`.
- Не встановлено фактичну версію Java для локального запуску: workflow фіксує лише Temurin JDK 8 у CI.
- Не визначено runtime-конфігурацію застосунку, міграції схеми БД та параметри підключення до PostgreSQL, оскільки відповідні файли застосунку й конфігурації не входять до переглянутого набору.
- Не перевірено фактичні транзитивні версії залежностей; у `build.gradle` для них задані лише координати без явних версій, які може керувати Spring Boot BOM.