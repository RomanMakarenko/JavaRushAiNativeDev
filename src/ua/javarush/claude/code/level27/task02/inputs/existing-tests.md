# Existing tests — що вже охоплено

Знімок поточного тестового покриття модуля `mrr-engine`.

| Сценарій | Тип тесту | Файл | Статус |
|----------|-----------|------|--------|
| refund carry-forward | characterization | `RefundCarryForwardCharacterizationTest` | зелений |
| plan switch proration | unit | `PlanSwitchProrationTest` | зелений |
| місячний snapshot | golden master | `MrrGoldenMasterTest` | нестабільний (потрібна нормалізація) |
| pause / resume в одному місяці | — | відсутній | біла пляма |

## Примітки

- pause / resume поки без characterization — закривається ручною перевіркою.
- golden master падає через нестабільні поля `generatedAt`, `traceId` і порядок планів.