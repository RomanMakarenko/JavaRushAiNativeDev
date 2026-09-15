# Поточний стан — сервіс cashflow

Знімок поточного стеку на момент migration discovery.

## Runtime і мова
- Java: 11 (`sourceCompatibility = VERSION_11` у `build.gradle`)
- Збірка: Gradle Wrapper 7.6.4

## Frameworks
- Spring Boot: 2.7.18
- Spring Security: 5.7.x (через starter)
- Hibernate ORM: 5.6.15.Final (оголошена напряму)

## Відомі болючі точки
- У персистентному шарі є кастомний `UserType` (`com.acme.cashflow.persistence.EncryptedStringType`),
  зав’язаний на Hibernate 5.x API. Поведінка на Hibernate 6.x не перевірялася.
- Security config написаний у стилі `WebSecurityConfigurerAdapter`.

## Цільовий стек (для контексту, не для виконання)
- Spring Boot 3.x, Java 21.