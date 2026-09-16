# Post-migration notes

## What changed
- The reporting time migration now includes a UTC-related step that normalizes timestamps into `normalized_ts_utc` during the migration flow.
- The local pilot has completed; this note records the handoff context without duplicating the detailed validation or rollback procedures.
- See [`MIGRATION_VALIDATION_REPORT.md`](MIGRATION_VALIDATION_REPORT.md) for the feature-parity validation details.

## How to run
- Use the migration runbook and configuration for the target environment; this document is a handoff summary, not an execution script.
- Before any production activity, confirm the intended phase, feature-flag state, and backup plan with the migration and release owners.
- For recovery instructions, follow [`ROLLBACK.md`](ROLLBACK.md).

## How validated
- Validation covered the UTC-normalized reporting path and comparison with the legacy representation on test data.
- The complete methodology, scope, and result are maintained in [`MIGRATION_VALIDATION_REPORT.md`](MIGRATION_VALIDATION_REPORT.md).

## Known issues
- **Limitation:** records older than two years were outside the validation sample, so parity for that historical range is not established.
- The recorded validation used a local PostgreSQL container; production behavior was not tested by that report.

## Rollback path
- If rollback is required, use the phase-specific steps in [`ROLLBACK.md`](ROLLBACK.md) rather than improvising a reversal from this summary.
- Confirm the rollback decision and communication with the release owner before changing migration flags.

## Owners
- Migration owner — coordinates phases, flags, and handoff decisions.
- Database owner — verifies backups, backfill behavior, and data integrity.
- Reports/application owner — verifies reporting behavior after UTC normalization.
- Release manager — approves rollout or rollback communication.

## Follow-up cleanup
- Extend validation to historical records older than two years before declaring the migration fully covered.
- Repeat the relevant checks in the target deployment environment before removing legacy compatibility.
- After the contract phase is approved, schedule cleanup of obsolete flags and legacy timestamp handling with the owners above.