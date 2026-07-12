# Review Summary

## summary
Diff додає метод `refresh()` до `AuthController`, який приймає refresh token, викликає `tokenService.resolveUser()` i `issueAccessToken()`. Код свідомо пропускає валідацію вхідного параметра та перевірку терміну дії токена — це задокументовано в Javadoc, але не виправлено. Вердикт: зміна потребує доопрацювання перед комітом.

## findings

1. **HIGH** — `AuthController.java:26-28` — Відсутня перевірка `refreshToken` на `null` / порожній рядок / невалідний токен. Якщо передати `null`, метод або кине `NullPointerException` в `resolveUser`, або, якщо той поверне `null`, кине NPE в `issueAccessToken(null)`. Javadoc на `:23-24` визнає це слабким місцем, але код не містить жодного захисту.

2. **HIGH** — `AuthControllerTest.java:10-24` — Немає тестів для нового методу `refresh()`. Існуючі тести покривають лише `login`. Метод із нуль-небезпечною сигнатурою (приймає `String refreshToken`) залишається без єдиного сценарію — ані успішного, ані граничного (`null`, порожній рядок, прострочений токен).

3. **MEDIUM** — `FakeTokenService.java:23-24` — `resolveUser` завжди повертає `"alice"` незалежно від вхідного значення. Це маскує всі потенційні проблеми з null/невалідним токеном у тестах, роблячи перевірку методу `refresh` через `FakeTokenService` безглуздою.

4. **LOW** — `AuthController.java:23` — Javadoc описує проблему замість того, щоб її виправити. Коментар «це і є слабке місце поточного diff» — це не інструкція для читача, а робоча нотатка, яка не повинна потрапити в коміт.

## tests/checks run
- Переглянуто всі файли проекту: `AuthController.java`, `TokenService.java`, `AuthException.java`, `AuthControllerTest.java`, `FakeTokenService.java`.
- Тести не запускалися (проект не збирається — відсутні залежності JUnit у директорії).

## uncertainty
- [гипотеза] Метод `resolveUser` може викидати власний тип винятку для невалідного/простроченого токена — тоді відсутність перевірки на null є менш критичною, але все одно варто явно ловити/документувати цей виняток у контракті методу `refresh`.
- [гипотеза] `issueAccessToken` може приймати `null` i працювати коректно — але це порушує очікування більшості API й не випливає з інтерфейсу `TokenService`.

## changed files
changed files: немає (read-only)

## next step
Додати перевірку `refreshToken` на `null` i порожній рядок на початку методу `refresh()` (кинути `AuthException`), замінити Javadoc-нотатку на нормальний опис контракту, та написати хоча б один тест для `refresh()` у `AuthControllerTest`, включно з граничним сценарієм (null token).