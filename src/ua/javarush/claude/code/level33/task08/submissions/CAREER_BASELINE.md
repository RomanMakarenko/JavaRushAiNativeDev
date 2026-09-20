# Career baseline

## Resume summary
Backend reliability engineer focused on billing and payment flows: reproduces reliability bugs, fixes retry-related failures, and verifies changes with reproducible tests. Core example: preventing duplicate payment charges with a minimal idempotent fix.

## Three resume bullets
- Reproduced a duplicate-charge bug during payment retries, wrote a regression test, and implemented a minimal idempotent fix. [Evidence: capstone repository](https://github.com/candidate/billing-reliability-lab)
- Added an `idempotency_key` and closed the race between two retry requests in the payment-charge flow. [Evidence: PR walkthrough](https://github.com/candidate/billing-reliability-lab/pull/12)
- Built a custom context-gathering command that collects relevant files and diagnostic command output for payment-flow bug investigations. [Evidence: workflow artifact](https://github.com/candidate/billing-reliability-lab/blob/main/.claude/commands/context-bug.md)

## GitHub profile intro
Backend reliability engineer working on billing and payment flows. I reproduce retry and idempotency failures, add focused regression tests, and make minimal fixes that are easy to verify. Current focus: reliable payment retries and duplicate-charge prevention.

## LinkedIn headline
Backend Reliability Engineer | Billing & Payment Flows | Retry and Idempotency Reliability

## LinkedIn about
I work on backend reliability in billing and payment flows, with a hands-on focus on reproducible evidence. My approach is to reproduce a failure, capture it in a test, and apply the smallest fix that addresses the demonstrated race or retry behavior.

A representative project reproduces a duplicate charge during payment retries and fixes it with an idempotency key, closing the race between two retry requests. I also built a custom context-gathering command to support focused bug investigations by collecting relevant files and diagnostic output.

Selected work:
- [Billing reliability lab](https://github.com/candidate/billing-reliability-lab)
- [PR walkthrough](https://github.com/candidate/billing-reliability-lab/pull/12)
- [Bug-investigation workflow artifact](https://github.com/candidate/billing-reliability-lab/blob/main/.claude/commands/context-bug.md)