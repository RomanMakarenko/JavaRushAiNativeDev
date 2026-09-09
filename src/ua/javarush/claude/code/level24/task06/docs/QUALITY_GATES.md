# Quality Gates

Локальні перевірки, які мають пройти до відкриття PR у `main`.

## Blocking

- `./gradlew test` — усі unit- і integration-тести зелені.
- `./gradlew check` — статичний аналіз без нових помилок.
- Немає секретів у diff (перевіряється hook на sensitive paths).

## Advisory

- Покриття нових рядків тестами не падає нижче baseline.
- Розмір PR — не більше одного логічного зміни.