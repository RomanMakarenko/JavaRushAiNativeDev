# Login Flow — Evidence & QA

> **Обмеження:** аналізується лише login flow — що відбувається під час виклику `POST /api/login`.
> Інші аспекти (security-заголовки, CORS, загальна архітектура) не розглядаються.

---

## Claim

При виклику `POST /api/login` додаток створює сесійний токен для будь-якого username, не перевіряючи пароль. Сесія зберігається в пам'яті й не має терміну дії. Cookie сесії не має атрибутів `Secure` та `SameSite`.

---

## Evidence

### 1. Маршрут і метод — `POST /api/login`

**Файл:** `src/main/java/com/acme/auth/AuthController.java`

- **Рядок 8:** `@RestController` — клас є REST-контролером.
- **Рядок 11-12:** Конструктор приймає `SessionStore` через DI.
- **Рядок 14:** `@PostMapping("/api/login")` — єдиний ендпоінт для логіну.
- **Рядок 15:** Сигнатура методу — `login(@RequestBody LoginRequest request, HttpServletResponse response)`.
- **Рядок 16:** `sessionStore.createSession(request.username())` — створює сесію на основі **виключно** `username`; поле `password` з `LoginRequest` у коді не згадується жодного разу.
- **Рядок 17:** `new Cookie("ACME_SESSION", sessionId)` — cookie без вказання `Secure`, `SameSite` або `Max-Age`.
- **Рядок 18:** `cookie.setHttpOnly(true)`.
- **Рядок 19:** `cookie.setPath("/")`.
- **Рядок 20:** `new LoginResponse("ok")` — єдиний варіант відповіді; альтернативних статусів (401, 403) у цьому методі немає.

### 2. Сховище сесій

**Файл:** `src/main/java/com/acme/auth/SessionStore.java`

- **Рядок 10:** `Map<String, String> sessions = new ConcurrentHashMap<>()` — in-memory сховище.
- **Рядок 11-14:** `createSession(String username)` — генерує `UUID.randomUUID().toString()`, кладе в мапу, повертає sessionId. Пароль не передається і не перевіряється.
- **Рядок 16-18:** `resolveUser(String sessionId)` — повертає `sessions.get(sessionId)`. Метод **ніде не викликається** — у робочій директорії (`src/main/java/`) немає жодного файлу, який би його використовував.

### 3. Доступність ендпоїнта

**Файл:** `src/main/java/com/acme/auth/SecurityConfig.java`

- **Рядок 14:** `.requestMatchers("/api/login").permitAll()` — `/api/login` є загальнодоступним.
- **Рядок 13:** `csrf.disable()` — захист CSRF вимкнено.

### 4. Тест

**Файл:** `src/test/java/com/acme/auth/AuthControllerTest.java`

- **Рядок 18-22:** Один тест-метод `login_setsSessionCookie()`.
- Надсилає `POST /api/login` з JSON `{"username":"alice","password":"secret"}`.
- Перевіряє: статус 200 + cookie `ACME_SESSION` присутній у відповіді.

### 5. Залежності

**Файл:** `build.gradle.kts`

- `spring-boot-starter-web` — веб-підтримка.
- `spring-boot-starter-security` — Spring Security.
- Відсутній драйвер БД (JPA/JDBC/R2DBC) — отже, дані користувачів не зчитуються з бази під час логіну.

---

## Assumptions

1. **LoginRequest має поле username (getter username())** — код викликає `request.username()`. Статус: **ПІДТВЕРДЖЕНО** (інакше код не компілювався б).
2. **LoginRequest має поле password** — тест передає `"password":"secret"` в JSON, але в контролері поле password не згадується. Статус: **НЕ ПІДТВЕРДЖЕНО** — ім'я та тип поля можуть відрізнятись.
3. **LoginResponse приймає рядковий аргумент** — код: `new LoginResponse("ok")`. Статус: **НЕ ПІДТВЕРДЖЕНО** — внутрішня структура невідома.
4. **UUID.randomUUID() використовує java.security.SecureRandom** — документація JDK гарантує 122 біти ентропії через SecureRandom. Статус: **ВВАЖАЄТЬСЯ ПІДТВЕРДЖЕНИМ**.

---

## Confidence

У цьому розділі перераховано лише твердження, що **безпосередньо стосуються login flow** і підкріплені прямими спостереженнями з файлів репозиторію.

- **Пароль не читається в `AuthController.login()`** — 100%. `SessionStore.createSession()` отримує лише `request.username()` (рядок 16 `AuthController.java`). Символ "password" не зустрічається в жодному .java-файлі.
- **Cookie `ACME_SESSION` не має `Secure`** — 100%. Конструктор `new Cookie("ACME_SESSION", sessionId)` приймає 2 параметри; `cookie.setSecure()` не викликається.
- **Cookie `ACME_SESSION` не має `SameSite`** — 100%. У `AuthController.java` немає виклику `cookie.setAttribute("SameSite", ...)`.
- **Сесія створюється без перевірки пароля** — 100%. `createSession(request.username())` — єдиний аргумент. Немає if/switch/assert/check, які б могли зупинити створення сесії.
- **`resolveUser()` ніде не використовується** — 100%. Grep по `src/main/java/` — метод оголошено в `SessionStore.java`, але жоден інший файл його не викликає.
- **Сесія зберігається in-memory (не в БД)** — 100%. `ConcurrentHashMap<String, String>` у `SessionStore.java`, відсутність залежностей БД у `build.gradle.kts`.