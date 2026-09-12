#!/usr/bin/env python3
"""Валідатор поля format у capstone-конфігу.

Перевіряє, що вибраний формат входить до закритого списку з шести
допустимих capstone-форматів. Бере шлях до JSON-конфігу першим аргументом.
"""

import json
import sys

# Закритий список допустимих форматів із CAPSTONE_BRIEF.md
ALLOWED_FORMATS = [
    "Feature in existing codebase",
    "AI-native MVP",
    "Legacy modernization mini-project",
    "Migration slice",
    "DevOps automation",
    "Team workflow design",
]


def main() -> int:
    if len(sys.argv) != 2:
        print("usage: validate_format.py <config.json>")
        return 2

    config_path = sys.argv[1]

    try:
        with open(config_path, encoding="utf-8") as f:
            data = json.load(f)
    except (OSError, json.JSONDecodeError) as exc:
        print(f"FAIL: не вдалося прочитати конфіг: {exc}")
        return 1

    fmt = data.get("format", "")

    if not fmt:
        print("FAIL: поле 'format' не заповнено")
        return 1

    if fmt not in ALLOWED_FORMATS:
        print(f"FAIL: формат '{fmt}' відсутній у закритому списку")
        return 1

    print(f"OK: вибрано допустимий формат '{fmt}'")
    return 0


if __name__ == "__main__":
    sys.exit(main())