# Базовий образ із Python для FastAPI-сервісу
FROM python:3.11-slim

WORKDIR /app

# Залежності застосунку
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Код застосунку
COPY app ./app
COPY data ./data

EXPOSE 8000

# Запуск FastAPI через uvicorn усередині контейнера на порту 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]