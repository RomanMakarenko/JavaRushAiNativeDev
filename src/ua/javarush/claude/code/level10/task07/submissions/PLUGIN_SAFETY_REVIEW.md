# Plugin Safety Review: `review-kit`

> **Review date:** 2026-07-06
> **Candidate version:** 0.3.0
> **Source directory:** `candidate-plugin/`

---

## Identity

| Field | Value |
|---|---|
| **Name** | `review-kit` |
| **Author** | `internal-tooling-team` |
| **License** | MIT |
| **Namespace** | `review-kit` |
| **Declared skills** | `issue-analysis`, `pr-review` |

**Verification:** All identity fields in `plugin.json` are present and internally consistent. The namespace `review-kit` matches the package name, which is good practice. The author `internal-tooling-team` is a team alias rather than a specific individual, making direct accountability diffuse — acceptable for an internal tool but worth noting.

---

## Capability Analysis

### Declared permissions (plugin.json lines 11–14)

| Permission | Risk Level | Justification in README |
|---|---|---|
| `network` | **Medium** | "Частина перевірок звертається до network, щоб підтягнути зовнішні правила review" |
| `shell` | **High** | "Використовується shell-скрипт для збирання diff" |

### Hooks (plugin.json lines 16–20)

| Hook Event | Command | Artifact Exists? |
|---|---|---|
| `post-commit` | `scripts/run.sh` | **No** — `candidate-plugin/scripts/` directory does not exist |

### Credential requirements

- `requiresCredentials: true` (line 15)
- README states: "Для публікації коментарів потрібен token із правами на репозиторій"

### Key Observations

1. **Missing script artifact.** The `post-commit` hook references `scripts/run.sh`, but that file is absent from the candidate bundle. Without it, reviewers cannot audit what the hook actually executes. This is a **blocking gap** for any `shell` permission — the most dangerous permission is granted without the corresponding source being reviewable.

2. **Dual high-risk permissions (`shell` + `network`).** The combination of `shell` and `network` means a compromised or malicious `scripts/run.sh` could exfiltrate data, install additional payloads, or tamper with the working tree — all over the network — without any additional prompting to the user.

3. **`post-commit` is a sensitive trigger.** The `post-commit` hook fires on every commit automatically. There is no opt-in gate or confirmation prompt described — the hook runs silently. If `scripts/run.sh` were present and, for example, pushed diffs to an external endpoint, every commit would leak repository contents.

4. **No source for the declared skills.** `plugin.json` declares two skills (`issue-analysis`, `pr-review`), but no `.js`/`.ts`/`.py` files implementing them are present in the bundle. It is unclear whether these skills are loaded from an external registry or simply missing.

---

## Maintenance Assessment

### Documentation

| Artifact | Status | Notes |
|---|---|---|
| `README.md` | ✅ Present | Describes purpose and key behaviours |
| `CHANGELOG.md` | ✅ Present | Covers v0.1.0 → v0.3.0 |
| `SUPPORT.md` | ✅ Present | Names owner and support channel |
| Rollback / disable instructions | ❌ Missing | SUPPORT.md explicitly notes: "про вимкнення і відкат поки не написано" |

### Version History

- **0.1.0** — Initial release: `issue-analysis` skill
- **0.2.0** — Added `pr-review` skill
- **0.3.0** — Added `post-commit` hook and network call to external review endpoint

### Support

- Owner: `internal-tooling-team`
- Channel: `#tooling-support` (internal chat)
- **Missing:** Escalation path, SLA, and — critically — procedures for disabling or rolling back the plugin.

---

## Decision

| Criterion | Verdict |
|---|---|
| Identity trustworthy? | ✅ Yes — fields are consistent, team is internal |
| Permissions justified? | ⚠️ Partial — `network` is justified, `shell` has a stated reason but its artefact is missing, making the justification unverifiable |
| Source auditable? | ❌ No — `scripts/run.sh` (the `shell` target) is absent; skill implementations are absent |
| Hooks safe? | ❌ Unknown — `post-commit` on every commit with no gate; content of the hook script is unknowable |
| Rollback possible? | ❌ No — explicitly undocumented |
| Overall risk profile | **High** — the combination of `shell` + `network` + `post-commit` + missing artefact creates a vector that cannot be assessed without the hook source |

**Decision:** `DO_NOT_INSTALL`

### ⚠️ Recommended Action

**Do not install v0.3.0 in its current state.** Before approval, the candidate must:

1. **Provide `scripts/run.sh`** for audit — without it, `shell` permission cannot be granted.
2. **Provide skill implementation files** or document how they are resolved (registry URL, built-in, etc.).
3. **Add a confirmation gate** before the `post-commit` hook sends data over the network (or document that one exists).
4. **Document disable/rollback procedures** in `SUPPORT.md`.
5. **Consider scoping down permissions** to `shell` only for diff collection (not general command execution), and require an explicit opt-in per-repository before the hook activates.

Once items 1–4 are addressed, a re-review can be conducted with the actual hook source in hand.