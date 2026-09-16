# Safe pilot candidates

## Preconditions

- Цільова платформа pilot: Spring Boot 3.2.x, Java 17 та Jakarta EE namespace.
- Перед початком pilot потрібно зафіксувати базову поведінку `reports/*`, прогнати наявний integration-тест `MonthlyRevenueControllerIT` і unit-тест `MonthlyRevenueServiceTest`, а також перевірити, що робоче дерево чисте щодо файлів застосунку.
- Pilot має бути обмежений модулем `reports` і не повинен включати зміни API, схеми даних, транзакцій або безпеки.
- Після змін потрібні: компіляція, повний набір тестів модуля `reports`, перевірка read-only endpoint та порівняння HTTP-контракту з базовим станом.
- Критерій зупинки: будь-яка зміна публічного контракту, падіння наявних тестів, відмінність статусів/тіла відповіді або необхідність торкнутися `billing` чи `security`.
- У цьому pilot не змінюються файли за межами `submissions/`; цей документ є єдиним запланованим артефактом.

## Pilot candidates

1. **`reports/MonthlyRevenueController.java` — рекомендований перший кандидат**
   - `COMPATIBILITY_MATRIX.md` оцінює сумісність як повну: використовуються лише `@RestController` і `@GetMapping` зі `spring-web`.
   - Контролер read-only, тому pilot не має зачіпати запис даних або транзакцій.
   - Покриття тестами — 92%, наявний `MonthlyRevenueControllerIT` дає сильний regression gate.
   - Історія змін стабільна: endpoint додано окремим комітом `[reports] add monthly revenue read-only endpoint`; відкритих паралельних гілок для `reports` не зазначено.
   - Безпечний pilot scope: перевірити сумісність/збірку під Spring Boot 3.x без зміни endpoint, його параметрів, статусів чи формату відповіді.

2. **`reports/MonthlyRevenueService.java` — другий кандидат у тому самому pilot**
   - Сервіс оцінений як повністю сумісний із цільовою платформою; імпортів `javax.*` немає.
   - Покриття — 88%, наявний `MonthlyRevenueServiceTest` забезпечує добрий unit-level regression gate.
   - Сервіс можна перевіряти після контролера, щоб ізолювати можливі проблеми інтеграції від проблем бізнес-логіки.
   - Безпечний pilot scope: перевірити компіляцію та незмінність результатів сервісу на наявних тестових даних, не змінюючи бізнес-правила.

## Exclude now

- **`billing/InvoiceProcessor.java`** — виключити: сумісність лише часткова, потрібна заміна `javax.persistence` на `jakarta.persistence`, що може зламати транзакції; покриття тестами лише 34%, integration-тестів немає.
- **`security/SecurityConfig.java`** — виключити: модуль не готовий до Spring Security 6, оскільки `WebSecurityConfigurerAdapter` вилучено; покриття 0%, тому regression signal відсутній і потрібна повна переробка.
- **Будь-які зміни в `billing` або `security` під час цього pilot** — виключити також через активні паралельні роботи `feature/tax-2026` та `feature/auth-hardening`.
- **Міграція бізнес-логіки, транзакцій, JWT, persistence або зміна публічного API** — не включати до safe pilot; це окремі задачі з власним планом, тестами та узгодженням.