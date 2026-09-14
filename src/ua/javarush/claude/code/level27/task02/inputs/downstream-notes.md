# Downstream notes — споживачі MRR-даних

Хто і як використовує вихід `mrr-engine`. Важливо для оцінки compatibility перед modernization.

## Billing reconciliation job

- Джерело: місячний MRR snapshot.
- Залежність: очікує, що refund поточного місяця відображається в MRR наступного місяця
  (поточний carry-forward quirk). Зміна цієї логіки зламає звірку без погодження.
- Це compatibility behavior: змінювати лише після міграції downstream.

## Finance dashboard (зовнішня вітрина)

- Джерело: місячний MRR snapshot (JSON).
- Чутливість: НЕ спирається на поля `generatedAt` і `traceId`, але падає,
  якщо порядок планів у snapshot змінюється між прогоном.

## Analytics export

- Джерело: значення `mrr[current]` по місяцях.
- Залежність від proration при plan switch — прийнятна, розбіжності згладжуються агрегацією.