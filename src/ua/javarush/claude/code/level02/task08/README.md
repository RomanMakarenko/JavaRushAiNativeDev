# billing-service

Сервіс білінгу: виставлення рахунків і повернення коштів (refund flow).
Бекенд на Java + Gradle, легка веб-панель на чистому JS.

## Запуск і тести

- Збірка: `./gradlew build`
- Тести: `./gradlew test`
- Локальний запуск: `./gradlew bootRun`
- Веб-панель: відкрити `web/index.html` у браузері

## Структура

- `src/main/java/com/rush/billing` — доменна логіка білінгу та повернень
- `web/` — статична панель оператора