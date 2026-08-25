# Refund Service

Сервіс обробки повернень для інтернет-магазину.

## Локальний запуск

1. Підніміть інфраструктуру (PostgreSQL, черга):

   ```bash
   docker compose up -d
   ```

2. Запустіть backend-тести:

   ```bash
   ./gradlew test
   ```

3. Перевірте frontend-лінтинг:

   ```bash
   npm run lint
   ```

## Перевірка документації

Документація пов’язана з кодом через docs-as-code перевірку. Запуск:

```bash
npm run docs:check
```

## API

Створення повернення: `POST /api/orders/{id}/refund`.