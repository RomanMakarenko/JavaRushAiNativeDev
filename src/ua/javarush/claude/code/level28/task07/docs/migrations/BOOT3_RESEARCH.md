# Boot 3.x research notes

Нотатки з офіційних джерел щодо переходу на Spring Boot 3.x.

## Базові вимоги
- Spring Boot 3.x вимагає Java 17 щонайменше. Java 21 — підтримувана LTS.
- Spring Boot 3.x вимагає Gradle 7.5+; для стабільної роботи рекомендована лінія 8.x.

## Hibernate
- Spring Boot 3.x переходить на Hibernate 6.x.
- У Hibernate 6.x змінено contract `UserType` — старі реалізації на 5.x не компілюються без правок.

## Jakarta namespace
- Boot 3.x використовує `jakarta.*` замість `javax.*` — це зачіпає persistence і web-анотації.

## Security
- `WebSecurityConfigurerAdapter` видалено в Spring Security 6 — потрібно переписати на component-based config.

## Відкриті питання
- Точний обсяг ручної адаптації кастомного `UserType` невідомий без прогону компіляції.