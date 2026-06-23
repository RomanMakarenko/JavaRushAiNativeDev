# Behavior Split: Refund Card Crash

## Current behavior
Opening a refund card that has no note attached results in a white screen. The card is unworkable.

## Desired behavior
If no note exists, display `No note provided`. If a note exists, display its text. No crash should occur.

## Hypotheses
- **Hypothesis 1** — The component accesses `note.text` without a null check. A missing note causes a null dereference.
- **Hypothesis 2** — The fix requires a single conditional in the render path.

## Evidence
Console shows `TypeError: Cannot read properties of null (reading 'text')` — the error fires at the exact moment the white screen appears.