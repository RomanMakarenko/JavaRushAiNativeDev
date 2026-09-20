// Сторінка interview prep: рендерить картки опор відповіді та список follow-up
// запитань. Вміст повністю надходить із data/interview.ts.

import { answerBlocks, followUpQuestions } from "../data/interview";

export default function InterviewPrepPage() {
  return (
    <main>
      <h1>Did you build this, or did AI?</h1>

      <section aria-label="answer-blocks">
        {answerBlocks.map((block) => (
          <article key={block.title}>
            <h2>{block.title}</h2>
            <ul>
              {block.points.map((point) => (
                <li key={point}>{point}</li>
              ))}
            </ul>
          </article>
        ))}
      </section>

      <section aria-label="follow-up-questions">
        <h2>Follow-up questions</h2>
        <ol>
          {followUpQuestions.map((question) => (
            <li key={question}>{question}</li>
          ))}
        </ol>
      </section>
    </main>
  );
}