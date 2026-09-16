# Матриця сумісності зі Spring Boot 3.x

Цільова платформа: Spring Boot 3.2.x, Java 17, Jakarta EE namespace (`javax.*` → `jakarta.*`).

| Модуль   | Файл                                    | Залежності                          | Сумісність | Примітки                                              |
|----------|-----------------------------------------|--------------------------------------|---------------|--------------------------------------------------------|
| reports  | reports/MonthlyRevenueController.java   | spring-web                           | Повна        | Лише `@RestController` і `@GetMapping`, міграція тривіальна |
| reports  | reports/MonthlyRevenueService.java      | spring-context                       | Повна        | Чистий Java-сервіс, імпорти `javax.*` відсутні      |
| billing  | billing/InvoiceProcessor.java           | spring-tx, javax.persistence, Jackson| Часткова     | Потребує заміни `javax.persistence` → `jakarta.persistence`, може зламати транзакції |
| security | security/SecurityConfig.java            | spring-security 5.x                  | Не готова     | `WebSecurityConfigurerAdapter` вилучено в Spring Security 6, потрібна повна переробка |

Висновок: модуль `reports` сумісний без змін API. `billing` і `security` потребують ризикованої ручної переробки.