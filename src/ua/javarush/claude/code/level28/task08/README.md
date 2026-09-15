# cashflow — фіксація динамічної версії в compatibility matrix

Дослідження лише для читання (28.4). `build.gradle` — матеріал для аналізу, його НЕ змінюємо.
У ньому залежність Hibernate задана динамічною маскою `5.+`, тому current
version не можна чесно записати в матрицю без фіксації точного числа.

Ваше завдання: заповнити рядок у `COMPATIBILITY_MATRIX.md`:
- зафіксувати факт маски `5.+` як ризик для baseline;
- взяти точну current version із evidence (вивід `./gradlew dependencies`);
- заміну маски на точну версію описати в «Потрібна дія» як ПЛАН (не правку);
- послатися на реальні джерела в «Докази».

Вхідні матеріали (лише для читання):
- `build.gradle` — рядок із `5.+`;
- `evidence/dependencies.txt` — вивід `./gradlew dependencies`.

Нічого не оновлюйте і не запускайте як deliverable. Цільову версію для upgrade
не обираємо — це discovery current state, а не execution.