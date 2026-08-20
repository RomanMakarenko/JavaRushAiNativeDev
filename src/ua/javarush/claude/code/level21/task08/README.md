# order-service

Мінімальний Spring Boot сервіс для керування замовленнями. Health endpoint доступний за адресою `/actuator/health` на порту `8080`.

## Збірка

```
./gradlew clean bootJar
```

## Запуск через Docker

```
docker build -t order-service:local .
docker run -d --name order-service -p 8080:8080 order-service:local
```

## Локальний цикл build → run → smoke → cleanup

Один скрипт виконує всю локальну перевірку сервісу однією командою:

```
scripts/package-local.sh
```

Послідовність кроків:

1. **build** — Gradle збирає fat jar, Docker будує образ `order-service:local`;
2. **run** — контейнер `order-service` стартує на порту `8080`;
3. **smoke** — curl опитує `/actuator/health`, поки сервіс не відповість статусом UP;
4. **cleanup** — контейнер і локальний образ видаляються (фаза виконується навіть при помилці).

Скрипт не комітить, не пушить і не публікує образ у registry — лише локальна перевірка.