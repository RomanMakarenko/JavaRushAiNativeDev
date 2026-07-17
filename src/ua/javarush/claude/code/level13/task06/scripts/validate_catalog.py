#!/usr/bin/env python3
"""Валідатор каталогу MCP-серверів.

Перевіряє, що кожен сервер у YAML-каталозі описано в суворій формі:
- є поля category, read_only_tools, write_tools;
- відомі сервери віднесено до правильних категорій;
- один і той самий tool не повторюється в read_only_tools і write_tools.

Парсер навмисно простий і не потребує зовнішніх залежностей: очікується каталог
виду "servers:" зі списком записів "- name: ...".
Друкує PASS або FAIL із причинами.
"""
import sys

# Очікувані категорії для відомих серверів.
EXPECTED_CATEGORY = {
    "issue-tracker": "issue-tracker",
    "pr-review": "source-control",
    "docs-lookup": "docs-lookup",
}


def parse_catalog(path):
    """Дуже простий парсер: повернути список словників по одному на сервер."""
    servers = []
    current = None
    current_list_key = None
    with open(path, encoding="utf-8") as f:
        for raw in f:
            line = raw.rstrip("\n")
            if not line.strip() or line.strip().startswith("#"):
                continue
            stripped = line.strip()
            indent = len(line) - len(line.lstrip(" "))
            # Початок нового запису сервера.
            if stripped.startswith("- name:"):
                current = {"name": stripped.split(":", 1)[1].strip(),
                          "read_only_tools": [], "write_tools": []}
                servers.append(current)
                current_list_key = None
                continue
            if current is None:
                continue
            # Елемент списку tools.
            if stripped.startswith("- ") and current_list_key:
                current[current_list_key].append(stripped[2:].strip())
                continue
            # Пара ключ: значення.
            if ":" in stripped:
                key, _, value = stripped.partition(":")
                key = key.strip()
                value = value.strip()
                if key in ("read_only_tools", "write_tools"):
                    current_list_key = key
                    if value:
                        current[key] = [v.strip() for v in
                                        value.strip("[]").split(",") if v.strip()]
                    else:
                        current[key] = []
                elif key in ("category", "name"):
                    current[key] = value
                    current_list_key = None
    return servers


def validate(path):
    servers = parse_catalog(path)
    errors = []
    if not servers:
        errors.append("у каталозі не знайдено жодного сервера")
    for s in servers:
        name = s.get("name", "<без імені>")
        if not s.get("category"):
            errors.append(f"{name}: відсутнє поле category")
        if "read_only_tools" not in s:
            errors.append(f"{name}: відсутнє поле read_only_tools")
        if "write_tools" not in s:
            errors.append(f"{name}: відсутнє поле write_tools")
        expected = EXPECTED_CATEGORY.get(name)
        if expected and s.get("category") != expected:
            errors.append(
                f"{name}: очікувалася категорія '{expected}', "
                f"отримано '{s.get('category')}'")
        overlap = set(s.get("read_only_tools", [])) & set(s.get("write_tools", []))
        if overlap:
            errors.append(
                f"{name}: tool повторюється в read_only_tools і write_tools: "
                f"{', '.join(sorted(overlap))}")
    return errors


def main():
    if len(sys.argv) != 2:
        print("Використання: python scripts/validate_catalog.py <catalog.yaml>")
        return 2
    errors = validate(sys.argv[1])
    if errors:
        print("FAIL")
        for e in errors:
            print(f"  - {e}")
        return 1
    print("PASS")
    return 0


if __name__ == "__main__":
    sys.exit(main())