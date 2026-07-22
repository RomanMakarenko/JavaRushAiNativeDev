"""Мінімальний mock docs-lookup server.

Read-only: повертає нотатки з каталогу fixtures/docs за ім'ям.
Жодних мережевих запитів і запису на диск — лише читання фікстур.
"""

import json
import os
import sys

FIXTURES_DIR = os.environ.get("DOCS_FIXTURES_DIR", "fixtures/docs")


def lookup(note_name: str) -> str:
    """Повернути текст нотатки за ім'ям (без розширення)."""
    path = os.path.join(FIXTURES_DIR, f"{note_name}.md")
    with open(path, "r", encoding="utf-8") as handle:
        return handle.read()


def main() -> None:
    # Найпростіший stdin/stdout протокол: на вхід ім'я нотатки, на вихід її текст
    note_name = sys.stdin.readline().strip()
    try:
        content = lookup(note_name)
        sys.stdout.write(json.dumps({"note": note_name, "content": content}))
    except FileNotFoundError:
        sys.stdout.write(json.dumps({"error": f"note not found: {note_name}"}))


if __name__ == "__main__":
    main()