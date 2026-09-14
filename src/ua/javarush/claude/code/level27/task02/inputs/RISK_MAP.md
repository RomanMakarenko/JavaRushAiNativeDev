# Risk map — модуль mrr-engine

Оцінка change risk перед modernization. Чим вищий ризик, тим суворіший baseline потрібен до змін.

| Область | Business criticality | Change risk | Unknowns |
|---------|----------------------|-------------|----------|
| refund carry-forward | висока | високий | downstream-звіти зав’язані на поточний quirk |
| plan switch (proration) | висока | середній | граничні дати всередині місяця |
| pause / resume | середня | середній | немає baseline для pause→resume в одному місяці |
| місячний snapshot | висока | високий | нестабільні поля `generatedAt`, `traceId`, порядок планів |

## Примітки

- refund carry-forward — найризикованіша ділянка: поведінка небажана, але downstream
  на неї спирається, ламати без baseline не можна.
- pause / resume — біла пляма, characterization відсутня.
- snapshot нестабільний і не годиться як golden master без нормалізації.