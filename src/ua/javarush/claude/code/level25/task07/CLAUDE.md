# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project goal

`subscription-tracker` — невеликий backend-сервіс для обліку підписок користувачів. Цільова функціональність: підрахунок активних підписок, помісячної виручки (MRR) і найближчих списань.

Поточний стан — початковий baseline: у директорії є README і Gradle-конфігурація, але реалізація та тести ще не додані. Не припускай наявність API, схеми даних або шару persistence, доки вони явно не з’являться в коді чи специфікації.

## Commands

Команди запускай з цієї директорії або з контексту, де доступний Gradle wrapper:

```bash
# Повна збірка
./gradlew build

# Усі тести
./gradlew test

# Один тест або метод JUnit 5 (заміни шаблон на фактичну назву)
./gradlew test --tests 'com.example.SomeTest.someCase'

# Локальний запуск application
./gradlew run
```

У `build.gradle.kts` налаштовано Kotlin/JVM `1.9.22`, JUnit Platform (`useJUnitPlatform()`), а entry point application — `MainKt`. Окреме lint-задачі в поточній конфігурації немає; не вигадуй команду lint — за потреби спочатку перевір доступні задачі через `./gradlew tasks --all`.

## Rules

- Дотримуйся Kotlin/JVM і Gradle Kotlin DSL, уже заданих у `build.gradle.kts`; нові залежності додавай лише для конкретної вимоги.
- Будь-які обчислення мають чітко розрізняти активні підписки, MRR і найближчі списання; правила включення/виключення зафіксуй у коді та тестах.
- Для тестів використовуй JUnit 5/Kotlin test і запуск через JUnit Platform, уже налаштований у Gradle.
- Зберігай `MainKt` як application entry point, якщо зміна точки входу не є частиною окремої вимоги.
- Не розширюй архітектуру фреймворками, HTTP або базою даних без вимоги й узгодженого контракту.

## Boundaries

- Працюй лише в `src/ua/javarush/claude/code/level25/task07`; не змінюй сусідні level/task-директорії або файли батьківського проєкту.
- Не видавай baseline за готовий backend: README прямо вказує, що основна робота ще не почалася.
- Не вигадуй публічні API, формат зберігання, часову зону чи політику billing, якщо їх не визначено в подальшій специфікації або коді.
- Не додавай lint, CI, Docker чи production deployment-конфігурацію як побічний обсяг — у поточному проєкті це не налаштовано.