# cashflow-mrr (legacy)

Успадкований сервіс розрахунку MRR (monthly recurring revenue) для платформи CashFlow.
Раз на добу знімає снапшот MRR за активними підписками.

## Збірка і тести

```
./gradlew build
./gradlew test
```

## Ключові точки

- `MrrSnapshotJob` — точка входу, запланована нічна задача.
- `MrrCalculator` — нормалізація цін і підсумовування MRR.
- `SubscriptionService` — вибірка активних підписок.
- `inputs/incident.md` — опис поточного інциденту з розбіжністю MRR.

> Сервіс у режимі discovery: оцінюємо ризик змін перед будь-якими правками.