"""Тести для update_display_name."""

from app.profile import update_display_name


def test_plain_name_is_saved_as_is():
    # Happy path: ім'я без зайвих пробілів зберігається без змін.
    assert update_display_name("Bob") == "Bob"
