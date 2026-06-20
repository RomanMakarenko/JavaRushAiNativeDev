# Запит на зміну

Від команди операторів повернень Commerce OS.

> На сторінці деталей повернення потрібно одразу бачити, що повернення потребує ручної
> перевірки. Якщо у refund поле `requiresManualReview` дорівнює `true`, показуй на
> detail page badge з текстом `Manual review required`.

Контекст:

- Об’єкт refund уже містить поле `requiresManualReview` (boolean).
- Backend і mock payload готові — змінювати їх не потрібно.
- Завдання обмежене frontend-шаром: компонент badge і його тест.
- Якщо `requiresManualReview` дорівнює `false`, badge показувати не потрібно.