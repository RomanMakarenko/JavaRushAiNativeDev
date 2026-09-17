from fastapi import FastAPI

# Demo-додаток Refund Assistant.
app = FastAPI()


@app.get("/api/demo/status")
def demo_status():
    """Статус demo-сценарію: тип артефакта, готовність даних, опис flow."""
    return {
        "artifactType": "demo",
        "sampleDataLoaded": True,
        "coreFlow": "Агент перевіряє refund за id заявки і бачить рішення з причиною.",
    }