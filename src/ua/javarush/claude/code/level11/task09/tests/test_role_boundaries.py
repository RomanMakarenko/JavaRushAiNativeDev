"""Перевірка меж ролей reviewer і tester на Layer 1 local review.

Тест читає agent-файли з .claude/agents і перевіряє, що:
- reviewer залишається read-only (немає write-capability),
- tester не править production code,
- в обох файлах є явні stop conditions.
"""
import re
from pathlib import Path

AGENTS_DIR = Path(__file__).resolve().parent.parent / ".claude" / "agents"

WRITE_TOOLS = {"edit", "write", "multiedit", "notebookedit"}


def _read(name: str) -> str:
    return (AGENTS_DIR / name).read_text(encoding="utf-8")


def _frontmatter_tools(text: str) -> set:
    """Дістає набір tools із YAML-frontmatter (рядок виду 'tools: A, B, C')."""
    m = re.search(r"^tools:\s*(.+)$", text, flags=re.MULTILINE)
    if not m:
        return set()
    return {t.strip().lower() for t in m.group(1).split(",") if t.strip()}


def test_reviewer_is_read_only():
    tools = _frontmatter_tools(_read("reviewer.md"))
    assert tools, "у reviewer має бути вказано tools у frontmatter"
    assert not (tools & WRITE_TOOLS), (
        "reviewer має бути read-only, знайдено write-tools: %s" % (tools & WRITE_TOOLS)
    )


def test_reviewer_does_not_implement():
    low = _read("reviewer.md").lower()
    assert "не редагує" in low or "read-only" in low
    assert "implementation" in low


def test_tester_does_not_edit_production_code():
    tools = _frontmatter_tools(_read("tester.md"))
    assert not (tools & WRITE_TOOLS), (
        "tester не повинен мати write-tools для product code: %s" % (tools & WRITE_TOOLS)
    )
    low = _read("tester.md").lower()
    assert "не правиш production code" in low or "не править production code" in low


def test_both_have_stop_conditions():
    for name in ("reviewer.md", "tester.md"):
        low = _read(name).lower()
        assert "stop conditions" in low, "у %s немає секції stop conditions" % name
        assert "поки не зафіксовані" not in low, "у %s stop conditions не заповнені" % name