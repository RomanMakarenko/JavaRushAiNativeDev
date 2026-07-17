# Маніфест MCP-сервера

name: docs-lookup
category: docs-lookup
mode: read-only

## Опис
Зовнішній сервер для пошуку в офіційній технічній документації. Категорія
docs lookup за своєю природою read-only: сервер нічого не змінює у зовнішніх системах.

## Інструменти
- search_docs (read-only) — пошук у документації
- get_doc_page (read-only) — отримати конкретну сторінку документації