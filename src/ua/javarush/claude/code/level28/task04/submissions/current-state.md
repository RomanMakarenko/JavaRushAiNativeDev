# Поточний стан міграції — CashFlow Dashboard

Discovery-документ, режим read-only. Заповніть розділи, спираючись на читання файлів
проекту та вивід команд. Нічого в проекті не міняйте. Кожне твердження
підтверджуйте file path і/або виводом команди; непідтверджене виносьте в Unknowns.

## Поточні версії
- Java: 8 — підтверджено CI-конфігом `.github/workflows/ci.yml:12-16`, де `actions/setup-java@v4` налаштовано з `distribution: temurin` і `java-version: '8'`.
- Spring Boot: 2.7.18 — підтверджено `build.gradle:2`.
- Gradle: версія не зафіксована в доступних файлах — `build.gradle` використовує Gradle DSL, а CI запускає `./gradlew`, але wrapper-файли в цій робочій директорії відсутні.
- PostgreSQL: версія не вказана; у `build.gradle:19` підтверджено лише runtime-залежність `org.postgresql:postgresql` без номера версії.

## Інструменти збірки та runtime
- Збірка: Gradle через Gradle Wrapper (`./gradlew`) — підтверджено CI-командою `.github/workflows/ci.yml:18` (`./gradlew clean test`) і Gradle-конфігурацією в `build.gradle`.
- Java baseline і чим він підтверджений: Java 8, Temurin — `.github/workflows/ci.yml:12-16`.
- Runtime-факти: CI використовує Ubuntu Latest (`.github/workflows/ci.yml:9`) і налаштовує Temurin Java 8 (`.github/workflows/ci.yml:12-16`). Локальний вивід `./gradlew -version` отримати не вдалося: `gradlew` у цій робочій директорії відсутній.

## Прямі та транзитивні залежності
- Прямі залежності з `build.gradle:16-20`: Spring Boot Web, Spring Boot Security, Spring Boot Data JPA, PostgreSQL JDBC-драйвер (runtime), Spring Boot Test (test).
- Транзитивні залежності окремо не визначалися; їх точний набір і версії невідомі.

## CI-команди
- `./gradlew clean test` — `.github/workflows/ci.yml:18`.

## Застарілі API
- Не досліджувалися в межах цього read-only збору evidence.

## Відомі збої
- Локальний Gradle Wrapper не присутній у цій робочій директорії, тому локальну команду `./gradlew -version` виконати неможливо.

## Unknowns
- Точна версія Gradle та версії Gradle Wrapper: wrapper-файл у доступній робочій директорії відсутній.
- Фактична версія PostgreSQL і версія JDBC-драйвера: номер версії не заданий у `build.gradle:19`.
- Результат виконання CI-команди та фактичні версії Java/Gradle у CI: вивід CI не наданий.
- Фактична локальна версія Java: команду `./gradlew -version` не виконано через відсутність `gradlew`.

## Нотатки про докази
- Основне evidence: `.github/workflows/ci.yml:9-18` (Ubuntu Latest, Temurin Java 8, команда `./gradlew clean test`).
- Додаткове evidence: `build.gradle:1-3,15-20` (Gradle Java-плагін, Spring Boot 2.7.18 і залежності).
- Перевірка файлів у межах робочої директорії показала, що `gradlew` і `gradle/wrapper/gradle-wrapper.properties` відсутні; вихід `./gradlew -version`: `gradlew: not present`.
