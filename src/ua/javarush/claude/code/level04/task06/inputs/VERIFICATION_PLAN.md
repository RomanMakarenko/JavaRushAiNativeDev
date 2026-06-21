# Verification plan (як надіслала команда)

## Commands
- ./gradlew test --tests "com.example.refunds.RefundServiceTest"
- ./gradlew test --tests "com.example.refunds.RefundIntegrationTest"

## Expected outputs
- Обидва прогони завершуються BUILD SUCCESSFUL.

## Інше, що потрапило в цей файл
- Дочекатися зеленого CI pipeline на гілці.
- Отримати approve від product owner на суму повернення.
- Перевірити, що навантажувальний тест на staging тримає 1000 rps.