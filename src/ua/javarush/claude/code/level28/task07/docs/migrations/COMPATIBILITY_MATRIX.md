# Compatibility matrix

Матриця сумісності для планування переходу з поточного стеку на цільовий стек
Spring Boot 3.x / Java 21. Статус кожного компонента використовує лише
фіксований словник, наведений нижче.

## Status vocabulary

- `safe update`
- `requires intermediate version`
- `blocked`
- `needs replacement`
- `needs code changes`
- `needs config changes`
- `needs manual validation`

## Matrix

| Component | Current state | Target state | Status | Required action | Evidence | Sequence constraint |
|---|---|---|---|---|---|---|
| Java | 11 (`sourceCompatibility = VERSION_11`) | 21 | `needs code changes` | Змінити source/target compatibility на Java 21, після чого виконати компіляцію та тести міграції. Перевірити код, який залежить від поведінки Java 11. | `docs/migrations/current-state.md:5`; `docs/migrations/BOOT3_RESEARCH.md:5` | Java 21 має бути доступна до перевірки збірки на Spring Boot 3.x. |
| Gradle | Wrapper 7.6.4 | Лінія 8.x | `needs manual validation` | Оновити Gradle Wrapper до погодженої версії 8.x і перевірити build, dependency resolution та всі task-и. | `docs/migrations/current-state.md:7`; `docs/migrations/BOOT3_RESEARCH.md:6`; `evidence/dependencies.txt:11` | Спочатку зафіксувати версію Gradle 8.x, потім валідовувати цільовий Boot 3.x стек. |
| Spring Boot | 2.7.18 | 3.x | `needs code changes` | Оновити Boot та пов’язані starters; виконати перехід `javax.*` → `jakarta.*` і повторити компіляцію та тести. | `docs/migrations/current-state.md:9`; `docs/migrations/BOOT3_RESEARCH.md:8-16`; `evidence/dependencies.txt:3-8` | Перевірка Spring Boot 3.x виконується після підготовки Java 21 і Gradle 8.x. |
| Hibernate ORM | 5.6.15.Final | 6.x через Spring Boot 3.x | `needs code changes` | Переписати `EncryptedStringType` під contract Hibernate 6.x; перевірити persistence, schema generation і runtime mapping. | `docs/migrations/current-state.md:11,14-15`; `docs/migrations/BOOT3_RESEARCH.md:8-10`; `evidence/dependencies.txt:5,8` | Спочатку оновити Boot до лінії, що використовує Hibernate 6.x, потім адаптувати й перевірити `UserType`. |
| Spring Security | 5.7.x | 6.x через Spring Boot 3.x | `needs code changes` | Переписати конфігурацію з `WebSecurityConfigurerAdapter` на component-based config і перевірити security integration tests. | `docs/migrations/current-state.md:10,16`; `docs/migrations/BOOT3_RESEARCH.md:15-16`; `evidence/dependencies.txt:6-7` | Переписування конфігурації має бути частиною переходу на Boot 3.x/Security 6, а не окремим safe update. |
| Jakarta namespace | `javax.*`-сумісний поточний стек | `jakarta.*` | `needs code changes` | Замінити імпорти та перевірити persistence/web анотації, конфігурацію і сумісність усіх application modules. | `docs/migrations/BOOT3_RESEARCH.md:12-13` | Виконати namespace migration разом з оновленням Spring Boot 3.x до компіляції та інтеграційної перевірки. |

## Planning notes

- Жоден компонент із неповним або лише теоретичним evidence не позначений як
  `safe update`.
- Matrix є planning input: фактичний обсяг змін треба підтвердити компіляцією,
  тестами та runtime validation після підготовки цільових версій.