"""Робота з профілем користувача."""


def update_display_name(name: str) -> str:
    """Зберігає display name користувача та повертає збережене значення.

    Баг: ім'я зберігається як є, без видалення провідних і замикальних пробілів.
    """
    # TODO: привести поведінку до acceptance criteria (trimming)
    saved = name
    return saved
