#!/usr/bin/env python3
"""Аудит ризиків MCP-конфігів.

Перевіряє JSON-конфіг(и) MCP-сервера на ознаки небезпечної поверхні:
широкі scopes, write-capable tools, hardcoded credentials і відсутність
обмежувачів виведення. Виводить PASS, якщо конфіг безпечний, інакше FAIL
зі списком проблем.

Використання:
    python scripts/audit_mcp_risks.py <config.json> [<config2.json> ...]
"""
import json
import re
import sys

# Маркери write-capable tools, яких не повинно бути в read-mostly наборі.
WRITE_TOOLS = {"post_comment", "merge_pr", "approve_pr", "update_issue", "close_issue"}

# Підозрілі на широкі права scopes.
BROAD_SCOPE_RE = re.compile(r"(admin:|:write\b|\bwrite\b)")

# Груба ознака hardcoded credential у значеннях конфіга.
SECRET_RE = re.compile(r"(sk_live_|sk_test_|ghp_|token\"\s*:\s*\"[^\"]+)")

# Ключі, наявність яких вважається обмежувачем виведення.
LIMIT_KEYS = {"limit", "pagesize", "page_size", "fields", "pagination"}


def audit(path):
    problems = []
    with open(path, "r", encoding="utf-8") as f:
        raw = f.read()
    data = json.loads(raw)

    tools = set(data.get("tools", []))
    write_present = tools & WRITE_TOOLS
    if write_present:
        problems.append(
            "active write-capable tools: " + ", ".join(sorted(write_present))
        )

    defaults = data.get("defaults", {})
    for tool in WRITE_TOOLS:
        if str(defaults.get(tool, "")).lower() in {"enabled", "true", "always"}:
            problems.append(f"write tool enabled by default: {tool}")

    for scope in data.get("authScopes", []):
        if BROAD_SCOPE_RE.search(scope):
            problems.append(f"broad auth scope: {scope}")

    if SECRET_RE.search(raw):
        problems.append("hardcoded credential detected in config")

    keys_lower = {k.lower() for k in _all_keys(data)}
    if not (keys_lower & LIMIT_KEYS):
        problems.append("no output limits (limit / page size / fields) found")

    return problems


def _all_keys(obj):
    keys = []
    if isinstance(obj, dict):
        for k, v in obj.items():
            keys.append(k)
            keys.extend(_all_keys(v))
    elif isinstance(obj, list):
        for item in obj:
            keys.extend(_all_keys(item))
    return keys


def main(argv):
    if len(argv) < 2:
        print("usage: python scripts/audit_mcp_risks.py <config.json> [...]")
        return 2
    all_problems = []
    for path in argv[1:]:
        all_problems.extend(f"{path}: {p}" for p in audit(path))
    if all_problems:
        print("FAIL")
        for p in all_problems:
            print("  - " + p)
        return 1
    print("PASS")
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))