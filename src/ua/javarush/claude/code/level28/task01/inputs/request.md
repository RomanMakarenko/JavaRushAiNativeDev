# Запит на зміну

Потрібно підняти CashFlow Dashboard на сучасний стек: перейти зі Spring Boot 2.7
на Boot 3.x і з Java 8 на Java 21. Старий namespace `javax.persistence` у JPA-класах
більше не підтримується новою версією framework — це й є основна проблема.

Заодно непогано було б почистити `BillingService` і виправити дивну поведінку timestamps,
але це вже окрема розмова.