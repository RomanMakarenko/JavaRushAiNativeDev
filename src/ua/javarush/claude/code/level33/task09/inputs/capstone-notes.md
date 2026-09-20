# Capstone notes — AI Commerce Growth OS, модуль refund inbox

## Що це за проєкт
AI Commerce Growth OS — e-commerce backend (Java 25 + Spring Boot 4, React 19).
Мій capstone-слайс: модуль refund inbox, задача RF-217 — оператор бачив запити
на повернення в неправильному порядку, через що старі термінові кейси тонули внизу.

## Що робив особисто я
- Написав `SPEC.md`: ціль, scope, non-goals, acceptance criteria.
- Зафіксував non-goal: не чіпати схему БД і не змінювати refund API.
- Вів реалізацію маленькими кроками, після кожного читав diff.
- Приймав або відхиляв кожну запропоновану зміну.

## Де допомагав Claude
- Дослідження codebase: де живе `RefundInboxService` і сортування.
- Чернетка реалізації компаратора за `createdAt`.
- Пропозиції щодо тестових сценаріїв (включно з порожнім inbox).
- Чернетка PR summary.

## Артефакти-якорі
- `SPEC.md`, `REVIEW_NOTES.md`, `DEMO.md`, PR #217.
- 6 регресійних тестів на сортування refund inbox.