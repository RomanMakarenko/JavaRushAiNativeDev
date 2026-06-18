# billing-service — project instructions

## Project commands

- Збірка: `./gradlew build`
- Тести: `./gradlew test`
- Локальний запуск: `./gradlew bootRun`
- Лінт веб-панелі: `npm run lint`

## Conventions

- Java-пакети у вихідному коді пишуться як `com.rush.*`.
- Гроші рахуємо лише через `BigDecimal`, ніяких `double` у розрахунках.

## Do-not

- Не комітити секрети, токени і реальні ключі платіжного провайдера.
- Не змінювати публічні сигнатури в `RefundService` без узгодження.