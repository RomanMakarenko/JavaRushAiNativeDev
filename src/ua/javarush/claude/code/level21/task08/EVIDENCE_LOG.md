# EVIDENCE_LOG — локальний цикл package-local.sh

Дата: 2026-08-20

## Прогін scripts/package-local.sh

Результат: **НЕУСПІХ** (exit 1)

- **build** — падіння: `bootJar` не знаходить main class («Main class name has not been
  configured»); у репозиторії немає вихідного коду Spring Boot (`src/main/java`).
  Gradle 8.10 з кешу wrapper підібрано коректно.
- **run / smoke** — не досягнуті (послідовність зупинилась на build).
- **cleanup** — виконано через trap EXIT (контейнер і образ `order-service:local` відсутні).

Примітка: повний успішний прогін стане можливим після додавання вихідного коду
застосунку з головним класом.