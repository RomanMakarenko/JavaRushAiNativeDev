# acme-billing-service

Біллінг-сервіс. Release flow побудований навколо межі artifact vs action:

- збір draft release notes — це artifact, його можна готувати автоматично;
- tagging і publish — це release actions, вони потребують окремого запуску людиною.

CI-конфігурація релізу лежить у `.github/workflows/release.yml`.
Допоміжні скрипти — у `scripts/`.