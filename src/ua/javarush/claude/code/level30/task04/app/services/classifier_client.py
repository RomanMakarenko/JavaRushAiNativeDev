"""Клієнт класифікації тем тікетів.

ПРОБЛЕМА (вихідний стан): клієнт завжди звертається до зовнішнього платного
TopClass Classify API. Локального demo-режиму немає.
"""

from app.settings import Settings


class ClassifierClient:
    """Повертає тему тікета за його текстом."""

    def __init__(self, settings: Settings) -> None:
        self.settings = settings

    def classify(self, ticket_text: str) -> str:
        # Завжди йдемо у зовнішній сервіс — для demo це ненадійно.
        return self._call_external_api(ticket_text)

    def _call_external_api(self, ticket_text: str) -> str:
        # Заглушка мережевого виклику до платного зовнішнього сервісу.
        # У реальному коді тут був би HTTP-запит з API token.
        raise RuntimeError("TopClass Classify API call requires a live external service")