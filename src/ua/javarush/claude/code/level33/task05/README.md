# Кар'єрний наратив — one-liner через Claude Code

Наскрізний кейс: той самий кандидат на Middle backend. У `inputs` лежать короткий evidence
summary і цільова роль. Потрібне одне коротке наративне речення, яке не змішує
рівні та одразу показує сильну сторону.

Робота ведеться в Claude Code: `/clear` → читання контексту → `/context` →
створення `submissions/NARRATIVE_ONE_LINER.md` → експорт trace через `/export`
у `submissions/SESSION_TRACE.md`.