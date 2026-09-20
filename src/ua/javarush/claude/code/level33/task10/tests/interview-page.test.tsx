// Тести interview prep page.
// Ці тести описують бажану поведінку: повну 4-step відповідь, п’ять follow-up
// запитань, відсутність overclaim і узгоджений порядок блоків.
// У поточному (з багом) стані даних частина тестів падає — це очікувано.

import { render, screen, within } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import InterviewPrepPage from "../app/page";
import { answerBlocks, followUpQuestions } from "../data/interview";

const expectedOrder = [
  "What I owned",
  "What Claude helped with",
  "What I verified manually",
  "What I'd improve next",
];

describe("interview prep page", () => {
  it("показує всі чотири опори відповіді", () => {
    render(<InterviewPrepPage />);
    for (const title of expectedOrder) {
      expect(screen.getByRole("heading", { name: title })).toBeTruthy();
    }
  });

  it("використовує узгоджений порядок чотирьох блоків", () => {
    expect(answerBlocks.map((block) => block.title)).toEqual(expectedOrder);
  });

  it("показує рівно п'ять follow-up запитань", () => {
    render(<InterviewPrepPage />);
    const section = screen.getByLabelText("follow-up-questions");
    const items = within(section).getAllByRole("listitem");
    expect(items).toHaveLength(5);
    expect(followUpQuestions).toHaveLength(5);
  });

  it("не містить overclaim про роль Claude", () => {
    render(<InterviewPrepPage />);
    expect(screen.queryByText(/Claude built everything for me/i)).toBeNull();
  });
});