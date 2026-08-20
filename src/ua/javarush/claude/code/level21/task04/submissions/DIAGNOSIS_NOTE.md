# Діагностична записка: payments-backend (task04)

## Likely root cause

Контейнер `backend` стартує без змінної середовища `PAYMENT_PROVIDER_MODE`. Backend вимагає її, щоб обрати адаптер платіжного провайдера. Значення змінної не передається ні через `environment` сервісу `backend` у [docker-compose.yml](../docker-compose.yml), ні через [.env.example](../.env.example), ні через інструкції [README.md](../README.md) (там лише загальне «Backend читає режим роботи платіжного провайдера зі змінної середовища» без назви змінної та значення). Унаслідок цього `ProviderConfig` не може обрати gateway-адаптер: на старті виникає ERROR, а health-перевірка деградує (статус, найімовірніше, не `UP`).

Вторинний недолік конфігурації: у [docker-compose.yml](../docker-compose.yml) мапінг портів `8081:8080` (зовнішній порт 8081), тоді як [README.md](../README.md) обіцяє сервіс на `http://localhost:8080`, тож smoke-команда з README вказує не на той порт.

## Evidence

- [logs/backend.log](../logs/backend.log) — реальний лог запуску (рядки 4, 5, 7), прямий запис того, що змінна не задана й адаптер не обрано:
  - `WARN ProviderConfig : Property PAYMENT_PROVIDER_MODE not set, falling back to default`
  - `ERROR ProviderConfig : No payment provider mode configured: cannot select gateway adapter`
  - `WARN ProviderHealth : Health check degraded: provider mode missing`

- [docker-compose.yml](../docker-compose.yml) — у блоці `environment` сервісу `backend` (рядки 7–13) перелічені лише `APP_PORT`, `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`; `PAYMENT_PROVIDER_MODE` відсутній. Портовий мапінг — `"8081:8080"` (рядок 6).

- [.env.example](../.env.example) — зразок середовища не містить `PAYMENT_PROVIDER_MODE`. Крім того, значення з `.env` потрапляють у контейнер лише через `${...}`-інтерполяцію в `docker-compose.yml`, а він усі значення хардкодить, тож сам факт створення `.env` проблему не вирішив би.

Секретів у цій записці немає; `DB_PASSWORD=changeme` у прикладах — плейсхолдер, а не реальний секрет.

## Next commands

1. Задати `PAYMENT_PROVIDER_MODE` для сервісу `backend` у `docker-compose.yml` (а також додати до `.env.example` і README), зі значенням, яке підтримує додаток (режим sandbox/stub платіжного провайдера).
2. Перезапустити сервіс: `docker compose up -d backend`
3. Переконатися, що змінна потрапила в контейнер: `docker compose exec backend env | grep PAYMENT_PROVIDER_MODE`
4. Smoke-перевірка на правильному зовнішньому порту: `curl http://localhost:8081/actuator/health` — очікувано `{"status":"UP"}`.