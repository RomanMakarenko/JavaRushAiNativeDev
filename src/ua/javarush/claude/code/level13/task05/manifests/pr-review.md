# Маніфест MCP-сервера

name: pr-review
category: source-control
mode: read-write

## Опис
Зовнішній сервер для роботи з pull request: читання diff, перегляд обговорень і
публікація коментарів review. Належить до категорії source control / PR.

## Інструменти
- get_pull_request (read-only) — метадані PR
- get_diff (read-only) — diff змін PR
- post_comment (write) — залишити коментар у PR
- merge_pull_request (write) — виконати merge PR