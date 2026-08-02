#!/usr/bin/env python3
"""Простий валідатор для Investigation Note.

Перевіряє, що в документі наявні обов'язкові секції розслідування.
Запуск: python tools/validate_investigation.py <шлях до INVESTIGATION_NOTE.md>
"""

import sys

# Обов'язкові секції розслідування (як заголовки Markdown).
REQUIRED_SECTIONS = [
    "Relevant files",
    "Related tests",
    "Likely change points",
    "Evidence",
    "Unknowns",
]


def main(argv: list[str]) -> int:
    if len(argv) != 1:
        print("Використання: validate_investigation.py <note.md>")
        return 2

    path = argv[0]
    try:
        with open(path, encoding="utf-8") as handle:
            text = handle.read()
    except OSError as error:
        print(f"Не вдалося відкрити файл: {error}")
        return 2

    # Вважаємо секцію наявною, якщо є Markdown-заголовок з її назвою.
    missing = []
    for section in REQUIRED_SECTIONS:
        marker = f"## {section}"
        if marker not in text:
            missing.append(section)

    if missing:
        print("INVALID: відсутні обов'язкові секції: " + ", ".join(missing))
        return 1

    print("VALID: усі обов'язкові секції на місці")
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))