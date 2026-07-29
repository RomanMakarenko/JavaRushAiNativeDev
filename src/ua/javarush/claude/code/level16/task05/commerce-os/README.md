# commerce-os

Навчальний комерційний сервіс. Скрипт `init.sh` розгортає git-репозиторій із гілкою
`main` (базовий стан модулів `checkout` і `promo`) та двома паралельними гілками:

- `feature/checkout-refactor` — рефакторинг обчислення підсумкової суми в шарі `checkout`.
- `feature/promo-discount` — зміни логіки promo-знижок.

Запустіть `bash init.sh` (або попросіть Claude Code виконати його) перед початком роботи.