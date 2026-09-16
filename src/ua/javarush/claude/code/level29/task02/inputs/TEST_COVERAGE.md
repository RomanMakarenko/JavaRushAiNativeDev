# Покриття тестами

| Файл                                  | Покриття | Тести                                      |
|---------------------------------------|----------|--------------------------------------------|
| reports/MonthlyRevenueController.java | 92%      | MonthlyRevenueControllerIT (integration)   |
| reports/MonthlyRevenueService.java    | 88%      | MonthlyRevenueServiceTest (unit)           |
| billing/InvoiceProcessor.java         | 34%      | частковий unit, немає integration          |
| security/SecurityConfig.java          | 0%       | тестів немає                               |

Висновок: `reports/*` добре покритий тестами і придатний для безпечної валідації pilot.
`billing` і `security` покриті слабо або взагалі не покриті — високий ризик регресій.