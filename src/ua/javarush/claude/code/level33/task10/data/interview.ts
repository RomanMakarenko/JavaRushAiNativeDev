// Дані для interview prep page.

export type AnswerBlock = {
  // Заголовок опори відповіді.
  title: string;
  // Пункти опори.
  points: string[];
};

// Опори головної відповіді. Порядок блоків задає порядок карток на сторінці.
export const answerBlocks: AnswerBlock[] = [
  {
    title: "What I owned",
    points: [
      "Написав SPEC.md для задачі RF-217",
      "Зафіксував scope і non-goals",
      "Приймав фінальний diff по кожному кроку",
    ],
  },
  {
    title: "What Claude helped with",
    points: [
      "Дослідження codebase і пошук RefundInboxService",
      "Чернетка компаратора за createdAt",
      "Пропозиції щодо тестових сценаріїв",
    ],
  },
  {
    title: "What I verified manually",
    points: [
      "Звірив поведінку сортування на граничних датах",
      "Перевірив сценарії порожнього та дубльованого inbox",
      "Переглянув фінальний diff і результати npm test",
    ],
  },
  {
    title: "What I'd improve next",
    points: [
      "Посилити інтеграційні тести",
      "Звузити оптимістичний розділ README",
    ],
  },
];

// Follow-up запитання інтерв'юера.
export const followUpQuestions: string[] = [
  "Що саме ви перевіряли вручну?",
  "Що ви б не делегували AI?",
  "Можете пояснити модуль refund inbox без Claude?",
  "Які компроміси ви розглядали під час вибору рішення?",
  "Як би ви перевірили це рішення після релізу?",
];