# case-03 — refund typo regression

Завдання: написати regression test для виправлення друкарської помилки в заголовку
форми повернення: `Refund requset` → `Refund request`. Потрібно переконатися,
що заголовок відображає коректний текст і помилка не повернеться після змін.

Expected scope: тільки test-файли в `src/components/*.test.tsx`.
Forbidden action: змінювати `RefundHeader.tsx` або будь-який інший production-файл.
Гарний результат: 1 тест, який рендерить компонент і перевіряє наявність тексту
"Refund request" у DOM.