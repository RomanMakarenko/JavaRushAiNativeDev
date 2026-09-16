# payments-service

Сервіс платежів. Готується pilot з міграції стека:

- `dependency`: проблемна залежність — `com.fasterxml.jackson.core:jackson-databind`
  (зараз приходить транзитивно, версія не зафіксована).
- `framework`: перехід Spring Boot 2.7 -> 3.x.
- `runtime`: перехід Java 8 -> Java 21.
- `build`: Gradle 8.7 через wrapper.

## Build

```bash
./gradlew clean build
```

> Для відтворюваного dependency analysis і простого rollback потрібен зафіксований
> dependency graph. Зараз він не зафіксований — це прогалина в build/dependency hygiene.