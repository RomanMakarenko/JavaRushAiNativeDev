# Маніфест MCP-сервера

name: monitoring
category: monitoring
mode: read-write

## Опис
Зовнішній сервер моніторингу й алертів Commerce OS. Дає змогу читати метрики та
стан алертів, а також підтверджувати (acknowledge) інциденти.

## Інструменти
- get_metric (read-only) — отримати значення метрики
- list_alerts (read-only) — список активних алертів
- ack_alert (write) — підтвердити алерт