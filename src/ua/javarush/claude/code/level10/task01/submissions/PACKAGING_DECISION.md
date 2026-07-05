# PACKAGING_DECISION

**Author:** Claude Code  
**Date:** 2026-07-05  
**Scope:** Level 10 Task 01 — packaging analysis of team workflows  

---

## Workflow: `issue-analysis`

### Decision

`PLUGIN_CANDIDATE`

### Consumers

- `commerce-os` — backend payment service
- `cashflow-dashboard` — payment analytics panel
- `workflow-kit` — shared engineering workflow kit

All three repositories in the team use this workflow.

### Evidence

**Source files referenced:** [`inputs/repo-list.md`](../inputs/repo-list.md), [`inputs/usage-signals.md`](../inputs/usage-signals.md), [`inputs/team-note.md`](../inputs/team-note.md)

1. **Cross-repo usage (repo-list.md + team-note.md):** The workflow exists in all three team repositories — `commerce-os`, `cashflow-dashboard`, and `workflow-kit`. The team note confirms that "almost everyone uses it": backend developers from Commerce OS, the Cashflow Dashboard team, and the shared Workflow Kit team all run `issue-analysis` when starting a new task.

2. **High call volume (usage-signals.md):** ~40 invocations in the last month across the three repos, indicating steady demand.

3. **Diverging copies (usage-signals.md):** The `cashflow-dashboard` copy already added a Non-goals step absent from the original, proving that local copies are beginning to drift. Centralising as a plugin would prevent further fragmentation and keep all teams on the same version.

### Open question

Should the Non-goals step added by `cashflow-dashboard` be adopted into the canonical plugin version, or should each team keep its own additions? This needs a team-wide sync before the plugin is published.

---

## Workflow: `project-setup`

### Decision

`KEEP_LOCAL`

### Consumers

- `commerce-os` — backend payment service

Only one repository uses this workflow.

### Evidence

**Source files referenced:** [`inputs/repo-list.md`](../inputs/repo-list.md), [`inputs/usage-signals.md`](../inputs/usage-signals.md), [`inputs/team-note.md`](../inputs/team-note.md)

1. **Single-repo usage (repo-list.md + team-note.md):** The workflow appears only in `commerce-os`. The team note states it is "only about Commerce OS" — it sets up the local environment specifically for that project's stack and seed data, and is useless in other repositories.

2. **Low call volume (usage-signals.md):** ~5 invocations in the last month, all within Commerce OS, confirming narrow scope.

3. **No copies exist (usage-signals.md):** Zero copies in other repositories, with no indication that other teams need or want it.

Promoting to a shared plugin is not justified: the workflow is tightly coupled to a single project's toolchain and shows no cross-team demand.

### Open question

None. The evidence clearly limits this workflow to a single repository with no cross-team interest.