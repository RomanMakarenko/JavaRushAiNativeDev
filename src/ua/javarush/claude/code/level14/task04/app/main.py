"""Вхідна точка FastAPI-застосунку сервісу повернень."""

from fastapi import FastAPI

# Застосунок працює за проксі з префіксом /api
app = FastAPI(title="store-refunds", root_path="/api")


@app.get("/health")
def health() -> dict:
    return {"status": "ok"}