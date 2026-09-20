# Commerce — PR walkthrough (checkout fix)

- Domain: e-commerce checkout.
- Проблема: за кількох знижок підсумкова сума кошика рахувалася двічі.
- Зона відповідальності розробника: локалізація бага в розрахунку total, minimal fix, regression test.
- Роль AI: генерація кандидатів гіпотез, розробник обирав і перевіряв.
- Verification: додано regression test на duplicate discount; локальна збірка зелена.
- Result: total рахується коректно за будь-якої кількості знижок.
- Публічне посилання: https://github.com/example/commerce/pull/142
- Limitations: виправлення покриває лише сценарій знижок, не весь pricing-движок.