# Spring Boot 3.0 Migration Guide (локальна копія)

> Офіційний гайд із переходу на Spring Boot 3.x. Authoritative source для міграції CashFlow.

## Java baseline

Spring Boot 3.x вимагає Java 17 як мінімальну версію (Java baseline).
Проєкти на Java 8 і Java 11 не підтримуються та мають бути оновлені до Java 17+.

## Jakarta EE 9 (javax -> jakarta)

У Spring Boot 3.x виконано перехід із простору імен `javax.*` на `jakarta.*`
(Jakarta EE 9). Імпорти `javax.persistence.*`, `javax.validation.*`,
`javax.servlet.*` мають бути замінені на відповідні `jakarta.*`.

## Spring Security

Клас `WebSecurityConfigurerAdapter` видалено. Конфігурація security переводиться
на component-based підхід із біном `SecurityFilterChain`. Старі DSL-ланцюжки
(`http.csrf().disable()` та подібні) переписуються на новий стиль.

## Configuration properties

Частина ключів конфігурації переїхала. Наприклад, `spring.redis.*` замінено на
`spring.data.redis.*`.