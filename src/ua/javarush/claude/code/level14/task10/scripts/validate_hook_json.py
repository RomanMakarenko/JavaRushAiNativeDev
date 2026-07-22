#!/usr/bin/env python3
"""Мінімальний валідатор hook-конфіга format-on-edit.

Перевіряє, що:
- файл існує і є валідним JSON;
- присутні обов'язкові поля name/event/matcher/handler;
- matcher.paths звужений і не дорівнює широкому frontend/src/**/* без розширення.

Використання:
    python3 scripts/validate_hook_json.py .claude/hooks/format-on-edit.json
"""
import json
import sys


def main() -> int:
    if len(sys.argv) != 2:
        print("usage: validate_hook_json.py <path-to-hook.json>", file=sys.stderr)
        return 2

    path = sys.argv[1]
    try:
        with open(path, encoding="utf-8") as handle:
            data = json.load(handle)
    except FileNotFoundError:
        print(f"FAIL: файл не знайдено: {path}", file=sys.stderr)
        return 1
    except json.JSONDecodeError as err:
        print(f"FAIL: невалідний JSON: {err}", file=sys.stderr)
        return 1

    for field in ("name", "event", "matcher", "handler"):
        if field not in data:
            print(f"FAIL: відсутнє обов'язкове поле: {field}", file=sys.stderr)
            return 1

    paths = data.get("matcher", {}).get("paths", [])
    if not paths:
        print("FAIL: matcher.paths порожній", file=sys.stderr)
        return 1

    for pattern in paths:
        # широкий glob без обмеження за розширенням вважаємо занадто загальним
        if pattern.rstrip("/").endswith("**/*"):
            print(
                f"FAIL: matcher надто широкий: {pattern} "
                "(обмежте розширенням, наприклад frontend/src/**/*.{ts,tsx})",
                file=sys.stderr,
            )
            return 1

    print("OK: hook-конфіг валідний, matcher достатньо вузький")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())