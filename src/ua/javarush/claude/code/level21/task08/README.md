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