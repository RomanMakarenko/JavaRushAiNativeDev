# payments-backend

Backend сервісу платежів на Spring Boot. Локально запускається через Docker Compose.

## Локальний запуск

```
docker compose up -d
```

Після запуску сервіс доступний за адресою `http://localhost:8080`.

## Smoke-перевірка

```
curl http://localhost:8080/actuator/health
```

Очікувана відповідь: `{"status":"UP"}`.

## Обов'язкові налаштування

Перед запуском скопіюйте `.env.example` у `.env` і заповніть значення.
Backend читає режим роботи платіжного провайдера зі змінної оточення.