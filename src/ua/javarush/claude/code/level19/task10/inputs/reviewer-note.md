# Reviewer note — AI_RefundRetryTest.java

Тест згенерований Claude Code для сценарію refund retry. Головне питання review:
**чи може цей тест пройти, поки баг усе ще існує?** Зараз — так, може.

## Проблема 1 — data hygiene

У тесті використовується `m.ivanova87@gmail.com` — це real-looking email, скопійований
з production-логів. У test data таким значенням не місце: потрібен нейтральний плейсхолдер
на кшталт `test@example.com`. Жодних secret-like значень (токени, ключі) у тесті бути не повинно.

## Проблема 2 — meaningless assertion / over-mocking

Єдина перевірка — `verify(refundRepository).save(any())`. Це перевіряє лише сам
факт виклику `save`, але не результат retry. Якщо повторна спроба поверне `FAILED`, запис
усе одно буде збережений, і тест залишиться зеленим — тобто пройде за живого бага.

## Що має бути у виправленому тесті

- Безпечні test data, без real-looking email і secret-like значень.
- Assertion на business behavior: після retry статус повернення має стати `SUCCEEDED`.
  Сценарій — перша спроба у провайдера падає (тимчасова помилка), retry доводить до успіху.
- Сценарне ім'я тестового методу замість `testRetry`.

## Контракт сервісу (для assertion)

`RefundRetryService.retryRefund(orderId, amount, customerEmail)` повертає `RefundResult`
з методом `status()` (рядок статусу повернення). Перевіряйте саме підсумковий статус:
`result.status()` має стати `"SUCCEEDED"` після успішного retry — це і є business
behavior, а не факт виклику `save(...)`.
