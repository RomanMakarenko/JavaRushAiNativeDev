# Резюме — Олексій Морозов

## Позиція
Junior Backend Engineer у команді білінгу Acme.

## Коротко
Початківець backend-розробник після курсу з AI-native розробки. Працюю з Java,
Spring Boot і REST API у навчальних та pet-проєктах. Практикую тестування,
small diffs, code review і відтворювані локальні перевірки.

## Релевантні технології та практики
- Java 21, Spring Boot 3, Gradle.
- REST API, JUnit 5, Testcontainers (на навчальному рівні).
- Git і базовий CI для pet-проєктів.
- Unit- та integration-тестування, characterization tests.
- Code review і дисципліна small diff.

## Релевантні проєкти

### Capstone: сервіс обліку підписок — solo, навчальний
- Спроєктував REST API backend-сервісу на Spring Boot з нуля.
- Реалізував unit-тести для бізнес-правил білінгу, зокрема розрахунку вартості підписки.
- Написав integration-тести REST-ендпоїнтів із Testcontainers.
- Підготував локальний CI-скрипт для запуску збірки й тестів перед комітом.
- Описав контракт довгої задачі у `SPEC.md` та evidence log.

### Навчальна міграція legacy-сервісу
- Підготував план міграції з фазами `expand → backfill → switch → contract`.
- Додав characterization tests, щоб зафіксувати поточну поведінку до змін.
- Виконував зміни окремими перевірюваними кроками за принципом small diff.

## AI-assisted workflow
- Використовую Claude Code у безпечному plan-first процесі: спочатку investigation note і план, далі small diffs, `/diff` та `/review`, а перед merge — human approval.

## Освіта
- Курс «Claude Code: AI-native Software Engineer».
- Самостійне вивчення Java і Spring за документацією.

## Формат досвіду
- Наведені приклади — навчальні та pet-проєкти; комерційний production-досвід, on-call і експлуатація під реальним навантаженням не заявляються.