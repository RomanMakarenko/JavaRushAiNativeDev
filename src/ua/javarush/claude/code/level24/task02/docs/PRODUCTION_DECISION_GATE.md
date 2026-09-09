# Production Decision Gate: REFUND-318

## Risk level

**high-risk** — зміна зачіпає критичний refund flow, перевірку доступу та схему production-бази даних.

## Evidence

- **Issue (REFUND-318):** потрібно підняти поріг автоматичного повернення та додати audit-таблицю політики повернень, оскільки частина повернень застрягає в ручному approval. Issue прямо зазначає вплив на refund flow і схему бази даних; відкат міграції потребує ручного кроку на production.
- **Changed files:** змінюються `src/main/java/com/example/commerce/refunds/RefundPolicyService.java`, `src/main/java/com/example/commerce/auth/RefundAccessGuard.java`, додається `db/migration/V104__refund_policy.sql` для таблиці `refund_policy_audit`, а також `README.md`.
- **CI summary (run #2451, branch `feature/REFUND-318`):** build — `SUCCESS`; unit tests — `142 passed, 0 failed`; integration tests — `28 passed, 0 failed`; migration dry-run — `SUCCESS` (V104 застосована на staging-копії); coverage — `81%` за порога `80%`.
- Усі обов’язкові погоджувачі мають статус `approved`, тому approval-доказ є повним.

## Required approvals

Обов’язкові погодження з `inputs/approvers.md`:

- **Marina Koval** — власниця бази даних, зона `db/migration/**` — `approved`.
- **Igor Petrov** — керівник з безпеки, зона `**/auth/**` — `approved`.
- **Anna Sokolova** — власниця домену refunds, зона бізнес-логіки refunds — `approved`.

## Rollback path

1. Зупинити або перевести в maintenance режим обробку нових автоматичних повернень, щоб під час відкату не створювалися нові записи за зміненою політикою.
2. Відкотити application release до попереднього перевіреного артефакту, який містить попередні версії `RefundPolicyService` та `RefundAccessGuard`; перевірити, що старий артефакт успішно запущений на всіх інстансах.
3. Після зупинки записів виконати на production ручний rollback для V104 за процедурою міграцій: видалити таблицю `refund_policy_audit` лише після backup/експорту її даних і перевірки відсутності активних залежностей; за потреби застосувати підготовлений зворотний SQL-скрипт замість ad-hoc команд.
4. Перевірити стан схеми та доступність refund flow через smoke/integration checks, переглянути помилки авторизації й помилки запису аудиту.
5. Відновити обробку повернень після підтвердження rollback відповідальними власниками та зафіксувати результат у deployment log.

## Final decision

**GO** — CI зелений, migration dry-run успішний, coverage відповідає порогу, а всі обов’язкові approver’и мають статус `approved`.