"""FastAPI-застосунок demo-ready-api.

Три ендпоінти для локальної демонстрації core flow:
- /api/health        — перевірка живості
- /api/demo-summary  — готовий підсумок за sample-даними
- /api/analyze       — аналіз переданого запиту
"""
import json
import os
from pathlib import Path

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="demo-ready-api")

# Корінь із sample-даними
DATA_DIR = Path(__file__).resolve().parent.parent / "data"


class AnalyzeRequest(BaseModel):
    """Вхідний запит на аналіз транзакцій."""
    user_id: str
    transactions: list[dict]


def _summarize(transactions: list[dict]) -> dict:
    """Обчислює короткий підсумок: суму, топ-категорію і статус."""
    total = round(sum(t.get("amount", 0) for t in transactions), 2)
    by_category: dict[str, float] = {}
    for t in transactions:
        cat = t.get("category", "other")
        by_category[cat] = by_category.get(cat, 0) + t.get("amount", 0)
    top_category = max(by_category, key=by_category.get) if by_category else "n/a"
    return {"total": total, "top_category": top_category, "status": "ok"}


@app.get("/api/health")
def health() -> dict:
    """Перевірка живості сервісу."""
    return {"status": "ok", "env": os.getenv("APP_ENV", "local")}


@app.get("/api/demo-summary")
def demo_summary() -> dict:
    """Готовий demo-підсумок за sample-запитом із data/."""
    sample = json.loads((DATA_DIR / "sample-request.json").read_text())
    summary = _summarize(sample["transactions"])
    return {"source": "sample-request.json", "summary": summary}


@app.post("/api/analyze")
def analyze(req: AnalyzeRequest) -> dict:
    """Основний core flow: аналіз переданих транзакцій."""
    return {"user_id": req.user_id, "summary": _summarize(req.transactions)}