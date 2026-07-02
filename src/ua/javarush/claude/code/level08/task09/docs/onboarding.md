# Онбординг нового розробника

Документ допомагає швидко підняти проєкт `shop` локально.

## Крок 1. Backend

Запустіть backend командою:

```
./gradlew bootRun
```

Перевірте, що сервіс піднявся, звернувшись до `GET /health`.

## Крок 2. Frontend

Перейдіть до каталогу `apps/web` і підніміть dev-сервер:

```
cd apps/web
npm install
npm run start
```

## Крок 3. Перевірка

Після запуску backend і frontend переконайтеся, що health endpoint відповідає `200 OK`.