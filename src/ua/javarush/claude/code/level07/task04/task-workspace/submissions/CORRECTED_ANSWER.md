# Виправлений аналіз login flow

---

## Хибне твердження №1

### Claim (з AI_ANSWER.md)

> *"Застосунок використовує stateless-автентифікацію на JWT."*
> *"Жодної серверної сесії немає."*

### Спростування

Застосунок використовує **stateful-автентифікацію на серверних сесіях, збережених у пам'яті**. Жодного JWT у коді немає.

### Evidence

**Джерело: `src/main/java/com/acme/auth/SessionStore.java`, рядки 10–13 (Javadoc) та 19–25 (код)**

```java
/**
 * Серверне сховище сесій.
 * Сесія живе на сервері: ідентифікатор генерується тут, а клієнту
 * повертається лише цей ідентифікатор через cookie. Жодного JWT і зберігання
 * токена на клієнті немає.
 */
@Component
public class SessionStore {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }
}
```

- `SessionStore` зберігає сесії **на сервері** у `ConcurrentHashMap`.
- Ідентифікатор сесії — це випадковий `UUID`, згенерований на сервері, а не JWT.
- Javadoc прямо каже: *"Жодного JWT"*.

**Джерело: `src/main/java/com/acme/auth/AuthController.java`, рядки 11–12 (Javadoc) та 26–33 (код)**

```java
/**
 * Логін приймає облікові дані, створює серверну сесію в SessionStore
 * і встановлює клієнту cookie з ідентифікатором сесії.
 */
    String sessionId = sessionStore.createSession(request.username());
    Cookie cookie = new Cookie("ACME_SESSION", sessionId);
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
```

- Контролер **створює серверну сесію** викликом `sessionStore.createSession()`.
- Клієнту передається лише `ACME_SESSION` cookie з ідентифікатором.
- Javadoc контролера підтверджує: *"створює серверну сесію"*.

**Джерело: `src/test/java/com/acme/auth/AuthControllerTest.java`, рядки 24–29**

```java
@Test
void login_setsSessionCookie() throws Exception {
    mockMvc.perform(post("/api/login")
            .contentType("application/json")
            .content("{\"username\":\"alice\",\"password\":\"secret\"}"))
           .andExpect(status().isOk())
           .andExpect(cookie().exists("ACME_SESSION"));
}
```

- Тест перевіряє наявність **cookie `ACME_SESSION`**, а не JWT-токена у тілі відповіді.

### Assumptions

AI припустило, що раз це Spring Boot REST API (найменування класів `AuthController`, `SecurityConfig`), то автентифікація має бути stateless JWT — як "типовий патерн". Однак припущення виявилося хибним: код використовує сесійну архітектуру.

### Confidence

**100%** — код і тести однозначно демонструють server-side session, без жодного JWT.

---

## Хибне твердження №2

### Claim (з AI_ANSWER.md)

> *"Після логіну сервер повертає JWT-токен, а фронтенд зберігає його в `localStorage` і додає до кожного запиту в заголовку `Authorization: Bearer <token>`."*
> *"Логін повертає токен у тілі відповіді."*

### Спростування

Сервер не повертає жодного токена в тілі відповіді. Ідентифікатор сесії передається виключно через **HttpOnly cookie**, що робить доступ через `localStorage` неможливим. Заголовок `Authorization` не використовується.

### Evidence

**Джерело: `src/main/java/com/acme/auth/AuthController.java`, рядки 29–35**

```java
Cookie cookie = new Cookie("ACME_SESSION", sessionId);
cookie.setHttpOnly(true);
cookie.setPath("/");
response.addCookie(cookie);

return new LoginResponse("ok");
```

- У тілі відповіді повертається `LoginResponse("ok")` — звичайний статус, а не токен.
- Cookie має прапорець `HttpOnly`, що забороняє JavaScript-доступ через `document.cookie` → збереження в `localStorage` неможливе.
- Cookie встановлюється з `path="/"`, тому браузер автоматично надсилатиме його на всі шляхи сервера — заголовок `Authorization` не потрібен.

**Джерело: `src/test/java/com/acme/auth/AuthControllerTest.java`, рядок 29**

```java
.andExpect(cookie().exists("ACME_SESSION"));
```

- Тест перевіряє саме наявність cookie, а не наявність токена в тілі. Якби в тілі був токен, тест, найімовірніше, мав би `.andExpect(jsonPath("$.token").exists())` або подібне.

**Джерело: `src/main/java/com/acme/auth/SessionStore.java`, рядок 13 (Javadoc)**

```
Жодного JWT і зберігання токена на клієнті немає.
```

- Документація SessionStore прямо заперечує існування JWT або токена, який зберігається на клієнті.

### Assumptions

AI припустило, що "логін повертає токен" — це стандартний шаблон, і не перевірило, що насправді повертається у відповіді. Також не було враховано, що `HttpOnly` cookie принципово не може бути прочитана або збережена JavaScript-кодом фронтенду.

### Confidence

**100%** — код контролера, SessionStore та тест однозначно підтверджують cookie-механізм без токенів.

---

## Зведена таблиця

| Твердження AI | Факт у коді | Джерело |
|---|---|---|
| Stateless JWT | Stateful server-side session (ConcurrentHashMap + UUID) | `SessionStore.java:19`, `AuthController.java:27` |
| Сервер повертає JWT-токен | Сервер повертає `LoginResponse("ok")` і HttpOnly cookie | `AuthController.java:30-35` |
| Токен у localStorage | Cookie HttpOnly — недоступна JavaScript | `AuthController.java:31` |
| Заголовок `Authorization: Bearer` | Cookie `ACME_SESSION` на `path="/"` | `AuthController.java:30-33` |
| Немає серверної сесії | Серверна сесія в ConcurrentHashMap | `SessionStore.java:19-24` |

---

### Незмінені / підтверджені частини AI_ANSWER.md

- Конфігурація `SecurityConfig` справді пропускає `/api/login` без автентифікації — **підтверджено** кодом `SecurityConfig.java`.
- Застосунок — Spring Boot REST API — **підтверджено** (`@RestController`, `@WebMvcTest` тощо).