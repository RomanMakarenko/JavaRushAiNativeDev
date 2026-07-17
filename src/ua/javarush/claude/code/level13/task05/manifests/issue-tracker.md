# Маніфест MCP-сервера

name: issue-tracker
category: issue-tracker
mode: read-write

## Опис
Зовнішній сервер трекера завдань команди Commerce OS. Використовується для intake вхідних
issue та оновлення статусів карток прямо з робочої сесії.

## Інструменти
- get_issue (read-only) — отримати картку issue за ключем
- list_issues (read-only) — список issue за фільтром
- create_issue (write) — створити нову картку
- update_issue (write) — змінити поля існуючої картки