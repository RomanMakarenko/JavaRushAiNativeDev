# Refund Tracker (capstone)

Невеликий сервіс обліку повернень (refund flow) для інтернет-магазину.
Це навчальний capstone-проєкт; після mentor review репозиторій готується до публікації.

## Що всередині
- core flow: створення заявки на повернення і зміна її статусу;
- матеріали review: `EVIDENCE.md`, `REMEDIATION.md`.

## Запуск перевірок
- тести: `pytest -q`;
- secret-scan: `grep -R "sk-live" .`.