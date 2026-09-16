# Карта типів міграції

Цей документ описує ризики пілотного оновлення `payments-service` за чотирма шарами: залежності, фреймворк, виконання та збірка. Карта спирається на `release-notes.md`, `build.gradle.kts`, `gradle/wrapper/gradle-wrapper.properties` і `.github/workflows/ci.yml`.

## Швидка карта

| Шар | Що змінюється | Типові ризики | Джерело підтвердження | Перша корисна перевірка |
|---|---|---|---|---|
| **Dependency / залежності** | Jackson `2.13 → 2.15` | Непередбачене оновлення транзитивної версії; помилки через `StreamReadConstraints`; зміна поведінки десеріалізації; розбіжність локального та CI-графа | `release-notes.md:10–13`; `build.gradle.kts:18–20` | Побудувати runtime-граф і перевірити фактичну версію Jackson: `./gradlew dependencyInsight --dependency jackson-databind --configuration runtimeClasspath` |
| **Framework / фреймворк** | Spring Boot `2.7 → 3.x` | Несумісні `javax.*` імпорти після переходу на `jakarta.*`; Java 17 як мінімальний baseline; зламані старі імпорти auto-configuration; зміни конфігурації стартера | `release-notes.md:5–8`; `build.gradle.kts:14–16` | Перевірити compile-залежності та виконати тестову компіляцію: `./gradlew compileKotlin compileTestKotlin test` |
| **Runtime / виконання** | Java `8 → 21` | Видалені застарілі GC-прапорці; зміна дефолтної поведінки JVM; відмінності між локальним JDK і CI; код ще компілюється під Java 8 | `release-notes.md:15–17`; `build.gradle.kts:28–32`; `.github/workflows/ci.yml:13–18` | Зафіксувати фактичні версії `java` і Gradle та прогнати тести на JDK 21: `java -version && ./gradlew --version && ./gradlew test` |
| **Build / збірка** | Gradle Wrapper `8.7` і відтворюваність CI | Невірний або недоступний wrapper-дистрибутив; розходження локального та CI-середовища; збірка перевіряється лише на Java 8; нестабільний dependency resolution без lock/constraints | `release-notes.md:19–21`; `gradle/wrapper/gradle-wrapper.properties:1–6`; `.github/workflows/ci.yml:8–21` | Перевірити wrapper і чисту збірку в тому самому режимі, що й CI: `./gradlew --version && ./gradlew clean test` |

## Деталізація шарів

### 1. Dependency — шар залежностей

**Об’єкт міграції:** Jackson `2.13 → 2.15`.

**Поточний стан:** `jackson-databind` оголошено без версії (`build.gradle.kts:18–20`), тому фактична версія приходить транзитивно та може змінюватися разом із framework-залежностями.

**Типові ризики:**

- фактична версія Jackson не збігається між локальною машиною, CI та майбутнім релізом;
- новий ліміт довжини рядка (`StreamReadConstraints`) відхиляє раніше прийнятні payload-и;
- зміни дефолтної поведінки десеріалізаторів дають помилки або непомітні зміни даних;
- ручне закріплення лише `jackson-databind` може залишити несумісний набір модулів Jackson різних версій.

**Джерело підтвердження:**

- `release-notes.md:10–13` фіксує зміни Jackson і те, що версію не закріплено;
- `build.gradle.kts:16–20` показує Spring Boot starter та незадекларовану версію `jackson-databind`.

**Перша корисна перевірка:**

```bash
./gradlew dependencyInsight \
  --dependency jackson-databind \
  --configuration runtimeClasspath
```

За результатом потрібно зафіксувати фактичну версію, джерело її вибору та весь узгоджений Jackson-набір у графі. Наступний практичний крок — додати тести на граничну довжину рядка і критичні сценарії десеріалізації.

### 2. Framework — шар фреймворку

**Об’єкт міграції:** Spring Boot `2.7.18 → 3.x`.

**Поточний стан:** проєкт безпосередньо використовує `org.springframework.boot:spring-boot-starter-web:2.7.18` (`build.gradle.kts:14–16`).

**Типові ризики:**

- `javax.*` більше не компілюється там, де потрібен відповідний `jakarta.*` пакет;
- поточна конфігурація або плагіни можуть не відповідати мінімальному Java baseline Spring Boot 3;
- старі імпорти та звернення до класів auto-configuration можуть стати недійсними;
- framework-оновлення одночасно змінить транзитивні залежності, зокрема Jackson, тому помилки можуть помилково виглядати як dependency-проблеми.

**Джерело підтвердження:**

- `release-notes.md:5–8` описує `javax.* → jakarta.*`, Java 17 як мінімум і переміщення частини auto-configuration;
- `build.gradle.kts:15–16` підтверджує поточну версію Spring Boot 2.7.18.

**Перша корисна перевірка:**

```bash
./gradlew compileKotlin compileTestKotlin test
```

Перевірку слід виконувати після підняття framework-залежності на окремому кроці міграції. Спочатку потрібно зібрати список compile-помилок і перевірити імпорти, конфігурацію та використання auto-configuration, не змішуючи ці результати з оптимізаціями runtime.

### 3. Runtime — шар виконання

**Об’єкт міграції:** Java `8 → 21`.

**Поточний стан:** Gradle все ще має `sourceCompatibility` і `targetCompatibility` на Java 8 (`build.gradle.kts:28–32`), а CI явно налаштовує Temurin Java 8 (`.github/workflows/ci.yml:13–18`). Водночас release notes визначають цільовий runtime як Java 21.

**Типові ризики:**

- запуск на Java 21 падає через видалені або несумісні старі GC-прапорці;
- змінюється поведінка JVM за замовчуванням, що може вплинути на latency, пам’ять і діагностику;
- локальна перевірка на новому JDK може не відтворювати CI, який досі працює на Java 8;
- source/target рівень Java 8 приховує проблеми сумісності та не дозволяє перевірити заявлений baseline Java 17/21;
- доступність sealed classes і pattern matching не означає автоматичної сумісності старого коду з новим runtime.

**Джерело підтвердження:**

- `release-notes.md:15–17` описує зміни GC, default-поведінки та можливості Java 21;
- `build.gradle.kts:28–32` показує, що toolchain ще не зафіксований і цілі залишаються Java 8;
- `.github/workflows/ci.yml:13–18` показує фактичний CI runtime Java 8.

**Перша корисна перевірка:**

```bash
java -version
./gradlew --version
./gradlew test
```

Цю перевірку треба повторити на JDK 21 і порівняти з результатом на поточному JDK 8. Окремо слід перевірити CI-конфігурацію та пошукати JVM-прапорці запуску, якщо вони з’являються в середовищі, скриптах або параметрах деплою.

### 4. Build — шар збірки

**Об’єкт міграції:** Gradle Wrapper `8.7` і процес відтворюваної збірки.

**Поточний стан:** wrapper завантажує `gradle-8.7-bin.zip` (`gradle/wrapper/gradle-wrapper.properties:1–6`), а CI запускає `./gradlew test` після checkout (`.github/workflows/ci.yml:8–21`).

**Типові ризики:**

- wrapper не може завантажити або перевірити заявлений дистрибутив;
- локальна збірка використовує іншу Java/Gradle-комбінацію, ніж CI;
- CI тестує лише Java 8 і тому не виявляє проблем переходу на Java 21;
- транзитивний dependency-граф змінюється без явного оновлення build-файлу;
- чиста машина або новий кеш відтворює інший результат, ніж розробницьке середовище.

**Джерело підтвердження:**

- `release-notes.md:19–21` вказує на Gradle 8.7, wrapper-дистрибутив і єдину Java-версію в CI;
- `gradle/wrapper/gradle-wrapper.properties:2–6` містить URL та параметри wrapper;
- `.github/workflows/ci.yml:8–21` показує фактичні CI-кроки.

**Перша корисна перевірка:**

```bash
./gradlew --version
./gradlew clean test
```

Перевірку варто виконати без покладання на вже прогріті локальні кеші, а потім повторити в CI. Після стабілізації runtime і dependency-шарів доцільно додати перевірку щонайменше для цільової Java 21, щоб build-шар не маскував runtime-ризики.

## Рекомендований порядок міграції

1. **Build:** підтвердити, що wrapper `8.7` відтворює чисту збірку.
2. **Runtime:** перевести перевірки на Java 21 і зафіксувати toolchain/CI baseline.
3. **Framework:** перейти зі Spring Boot 2.7 на 3.x та виправити `javax.*`/`jakarta.*` і auto-configuration несумісності.
4. **Dependency:** після стабілізації framework-графа визначити та закріпити узгоджену версію Jackson, потім перевірити обмеження парсингу й десеріалізацію.
5. **Regression:** виконати чисту збірку і тести в CI на цільовому runtime; окремо зберегти результати `dependencyInsight` як підтвердження фактичного графа.

Це лише карта перевірок: код застосунку, `build.gradle.kts`, CI та wrapper у межах цього завдання не змінюються.