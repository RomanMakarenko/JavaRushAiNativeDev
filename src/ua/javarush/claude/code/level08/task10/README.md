# shop

Навчальний сервіс інтернет-магазину. Backend — Spring Boot (Gradle), frontend — `apps/web`.

## Запуск backend

```
./gradlew bootRun
```

## Запуск frontend

```
cd apps/web
npm install
npm run dev
```

## Health check

Сервіс повертає стан за адресою `GET /api/health`.