#!/usr/bin/env python3
"""Валідатор політики використання AI у тестових завданнях.

Перевіряє, що YAML-конфіг описує три дозволені статуси,
задає безпечний default і містить дії для кожного статусу.
Використовує лише стандартну бібліотеку (жодних зовнішніх залежностей).
"""
import sys
import re

# Дозволені статуси політики згідно з матеріалами лекції
ALLOWED_STATUSES = {"forbidden", "allowed-with-disclosure", "ask-first"}


def load_yaml_lite(path):
    """Мініпарсер плоского YAML без зовнішніх залежностей.

    Підтримує скаляри `ключ: значення` і однорівневі секції
    виду `ключ:` з вкладеними `  ім'я: текст`. Цього достатньо
    для перевірюваного конфіга.
    """
    data = {}
    current_section = None
    with open(path, encoding="utf-8") as f:
        for raw in f:
            line = raw.rstrip("\n")
            if not line.strip() or line.lstrip().startswith("#"):
                continue
            indent = len(line) - len(line.lstrip(" "))
            key, _, value = line.strip().partition(":")
            key = key.strip()
            value = value.strip()
            if indent == 0:
                if value == "":
                    data[key] = {}
                    current_section = key
                else:
                    data[key] = value.strip('"').strip("'")
                    current_section = None
            else:
                if current_section is not None:
                    data[current_section][key] = value.strip('"').strip("'")
    return data


def main():
    if len(sys.argv) != 2:
        print("usage: validate_assessment_policy.py <config.yml>")
        return 1
    path = sys.argv[1]
    try:
        data = load_yaml_lite(path)
    except OSError as exc:
        print(f"FAIL: не вдалося прочитати конфіг: {exc}")
        return 1

    statuses = data.get("statuses")
    if not isinstance(statuses, dict) or not statuses:
        print("FAIL: відсутній розділ statuses")
        return 1

    declared = set(statuses.keys())
    if declared != ALLOWED_STATUSES:
        print(f"FAIL: очікувалися статуси {sorted(ALLOWED_STATUSES)}, знайдено {sorted(declared)}")
        return 1

    for name, action in statuses.items():
        if not str(action).strip():
            print(f"FAIL: для статусу {name} не задано дію")
            return 1

    default = str(data.get("default_when_unknown", "")).strip()
    if default == "allowed":
        print("FAIL: відсутність policy трактується як allowed")
        return 1
    if default != "ask-first":
        print(f"FAIL: безпечний default має бути ask-first, знайдено {default!r}")
        return 1

    print("OK")
    return 0


if __name__ == "__main__":
    sys.exit(main())