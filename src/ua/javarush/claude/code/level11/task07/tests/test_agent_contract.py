"""Перевірка 4-елементного contract у .claude/agents/reviewer.md.

Тест читає instruction body агента й переконується, що в ньому є всі
обов'язкові секції, вимога до evidence у findings, явна заборона на
редагування коду, stop condition для широкого/чутливого diff і відсутність
roleplay-формулювань.
"""
from pathlib import Path

AGENT_FILE = Path(__file__).resolve().parent.parent / ".claude" / "agents" / "reviewer.md"


def read_agent() -> str:
    assert AGENT_FILE.exists(), f"Не знайдено файл агента: {AGENT_FILE}"
    return AGENT_FILE.read_text(encoding="utf-8")


def test_required_sections_present():
    text = read_agent()
    for section in (
        "## Role",
        "## When to use",
        "## Allowed actions / Output",
        "## Stop conditions",
    ):
        assert section in text, f"Відсутня обов'язкова секція: {section}"


def test_output_requires_evidence():
    text = read_agent().lower()
    # findings повинні вимагати severity, file:line і evidence
    assert "severity" in text, "В output не вказано severity для findings"
    assert "file:line" in text, "В output не вказано формат file:line"
    assert "evidence" in text, "В output не вказано evidence requirement"


def test_no_code_edits_phrase():
    text = read_agent().lower()
    assert "не редактир" in text or "не правит код" in text or "does not edit" in text, (
        "Немає явної фрази про те, що агент не редагує код"
    )


def test_stop_condition_for_broad_or_sensitive_diff():
    text = read_agent().lower()
    has_broad = "широк" in text or "broad" in text
    has_sensitive = "sensitive" in text or "чувствительн" in text
    assert has_broad and has_sensitive, (
        "У Stop conditions немає випадку занадто широкого diff або sensitive area"
    )


def test_no_roleplay_phrasing():
    text = read_agent().lower()
    forbidden = ["представь, что ты", "ты senior", "веди себя как", "act as a senior"]
    for phrase in forbidden:
        assert phrase not in text, f"У тексті залишилася roleplay-формулювання: {phrase}"