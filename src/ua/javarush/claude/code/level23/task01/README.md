# Workflow Kit

Набір інструментів і шаблонів для підготовки AI-assisted завдань Commerce OS.

## Стартовий артефакт задачі
Кожне нове завдання описується YAML-файлом у `workflow/`. Перед початком роботи
до файлу додають risk label і capability envelope.

## Перевірка
```
python tools/validate-task.py workflow/new-task.yaml
```
Валідатор друкує `PASS` або `FAIL` і поле, на якому зупинився.