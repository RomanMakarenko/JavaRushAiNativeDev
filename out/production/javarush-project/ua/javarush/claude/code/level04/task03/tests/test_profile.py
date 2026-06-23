"""Тести для update_display_name."""

from app.profile import update_display_name


def test_plain_name_is_saved_as_is():
    # AC 2 (regression): ім'я без зайвих пробілів зберігається без змін.
    assert update_display_name("Bob") == "Bob"


def test_leading_and_trailing_spaces_are_trimmed():
    # AC 1: провідні та замикальні пробіли видаляються.
    assert update_display_name("  Alice  ") == "Alice"


def test_only_spaces_returns_empty_string():
    # AC 3: рядок з самих пробілів повертає порожній рядок.
    assert update_display_name("   ") == ""


def test_leading_spaces_only_are_trimmed():
    # AC 4: пробіли лише з лівого боку обрізаються.
    assert update_display_name("  Jane") == "Jane"


def test_trailing_spaces_only_are_trimmed():
    # AC 4: пробіли лише з правого боку обрізаються.
    assert update_display_name("John ") == "John"


def test_inner_spaces_are_preserved():
    # AC 4 (regression): пробіли всередині імені не зачіпаються.
    assert update_display_name("A B") == "A B"
