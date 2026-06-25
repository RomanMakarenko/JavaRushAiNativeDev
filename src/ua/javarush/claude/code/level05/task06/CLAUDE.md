# CLAUDE.md — Commerce OS (auth-модуль)

## Про проєкт

Commerce OS — backend на Java 25 + Spring Boot 4. Цей workspace обмежений
auth-шаром: вхід, валідація облікових даних і рендер форми логіну.

## Правила роботи

- Змінюй лише те, що стосується поточної задачі з `TASK_SPEC.md`.
- Не чіпай public API session-шару без явної необхідності.
- Не додавай нові зовнішні залежності.
- Перед зміною коду читай `TASK_SPEC.md` і зачеплені файли.

## Команди перевірки

- `./gradlew test` — прогін тестів модуля.
- `./gradlew test --tests AuthFlowTest` — лише auth-сценарії.