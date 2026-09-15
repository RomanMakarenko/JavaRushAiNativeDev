# CashFlow Dashboard

Legacy-сервіс обліку підписок і розрахунку виручки.

## Поточний стек
- Spring Boot 2.7.18
- PostgreSQL 12 (локально через docker-compose)
- Збірка: Gradle Wrapper

## Локальний запуск
```bash
./gradlew help
./gradlew clean test
```

> Java baseline проекту історично не зафіксований у `build.gradle` явно — збірка спирається на локальну JDK. Це факт поточного стану (current state), що фіксується на етапі discovery.
