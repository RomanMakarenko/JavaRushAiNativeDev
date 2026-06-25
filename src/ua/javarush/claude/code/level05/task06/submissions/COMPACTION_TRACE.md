# COMPACTION_TRACE

> Команда: `/compact`
> Інструкція: `збережи goal, scope і constraints`

## Що зберегла система (compact)

Система виконала `/compact` — стиснення історії розмови до summary.
У summary збережено:
- Факт двох проходів аналізу (TASK_SPEC.md, LoginController.java, AuthFlowTest.java)
- Контекст проєкту: Java 25 + Spring Boot 4, auth-модуль Commerce OS
- Баг: `return "";` у `LoginController.submit()` замість `return "auth/login";`
- Тест `wrongPasswordShowsErrorMessage()` у `AuthFlowTest.java` викликає `renderLoginError()` який повертає `""`

## Що потрібно заново (restate після compaction)

### Goal
Виправити баг — після невірного пароля користувач бачить білий екран замість форми логіну з повідомленням про помилку.

### Scope
- `src/auth/LoginController.java` — змінити `return "";` на `return "auth/login";`
- `tests/AuthFlowTest.java` — виправити `renderLoginError()` (рядки 38-39)

### Constraints
- Не змінювати public API session-шару
- Не чіпати DB міграції
- Не додавати зовнішніх залежностей
- Після фіксу успішний логін має продовжувати працювати

### План перечитування після compaction
1. `TASK_SPEC.md` — поновити вимоги
2. `src/auth/LoginController.java` — поновити код контролера
3. `tests/AuthFlowTest.java` — поновити код тесту

### Стан на момент compaction
Файли не змінені, коміт `bfbf4e7` (l5 t6). Зміни ще не вносились — тільки аналіз.