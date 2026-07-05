#!/usr/bin/env python3
"""Локальний валідатор анатомії SKILL.md.

Перевіряє, що у skill-файла є YAML-frontmatter з обов'язковими полями,
а в body присутні розділи output contract і constraints.

Використання:
    python3 scripts/validate_skill_frontmatter.py <путь до SKILL.md>

Код повернення 0 — усі перевірки пройдено, 1 — є помилки.
"""
import sys

# Обов'язкові ключі frontmatter (core-поля анатомії skill).
REQUIRED_FRONTMATTER = ["name", "description", "when_to_use", "arguments", "allowed_tools"]

# Обов'язкові розділи тіла skill.
REQUIRED_BODY_SECTIONS = ["Expected output", "Constraints"]


def split_frontmatter(text):
    """Повернути (frontmatter, body); frontmatter == None, якщо блоку немає."""
    lines = text.splitlines()
    if not lines or lines[0].strip() != "---":
        return None, text
    for i in range(1, len(lines)):
        if lines[i].strip() == "---":
            return "\n".join(lines[1:i]), "\n".join(lines[i + 1:])
    return None, text


def main():
    if len(sys.argv) != 2:
        print("ПОМИЛКА: вкажіть шлях до SKILL.md", file=sys.stderr)
        return 1

    path = sys.argv[1]
    try:
        with open(path, encoding="utf-8") as f:
            text = f.read()
    except OSError as exc:
        print(f"ПОМИЛКА: не вдалося прочитати файл: {exc}", file=sys.stderr)
        return 1

    frontmatter, body = split_frontmatter(text)
    errors = []

    if frontmatter is None:
        errors.append("відсутній YAML-frontmatter (блок між --- і ---)")
    else:
        for key in REQUIRED_FRONTMATTER:
            if f"{key}:" not in frontmatter:
                errors.append(f"у frontmatter немає обов'язкового поля: {key}")

    for section in REQUIRED_BODY_SECTIONS:
        if section not in body:
            errors.append(f"у body немає обов'язкового розділу: {section}")

    if errors:
        print(f"FAIL: {path}")
        for err in errors:
            print(f"  - {err}")
        return 1

    print(f"OK: {path} — анатомія skill коректна")
    return 0


if __name__ == "__main__":
    sys.exit(main())