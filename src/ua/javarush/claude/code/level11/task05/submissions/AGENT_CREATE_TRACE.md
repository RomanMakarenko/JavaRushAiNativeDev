# AGENT CREATE TRACE

## Command

```
/agents
```

The `/agents` wizard was invoked to create a new custom subagent.

---

## Step 1 — Agent Name

**Prompt:** Enter a name for your agent (kebab-case, e.g. `my-agent`).

**Response:** `reviewer`

---

## Step 2 — Scope

**Prompt:** Where do you want to save this agent?

- `project` — `.claude/agents/` (this project only)
- `global` — `~/.claude/agents/` (all projects)

**Response:** `project`

**Result:** Agent will be saved at `.claude/agents/reviewer.md` (project-level).

---

## Step 3 — Description

**Prompt:** Write a short, one-line description of what this agent does.

**Response:** `Reviews the finished diff and returns comments without editing the code`

---

## Step 4 — Model

**Prompt:** Which model should the agent use?

Available options:
- *Inherit from session* (default)
- `claude-sonnet-5`
- `claude-haiku-4-5-20251001`
- Custom

**Response:** `claude-sonnet-5`

---

## Step 5 — Reasoning Effort

**Prompt:** How much reasoning effort should the agent use?

- `low` — quick, shallow analysis
- `medium`
- `high` — detailed analysis
- `xhigh`
- `max` — maximum depth

**Response:** `high`

---

## Step 6 — Tools

**Prompt:** Select the tools this agent is allowed to use.

Available tools:
- `Read` — read files
- `Grep` — text search
- `Glob` — file search by pattern
- `Bash` — shell commands
- `Edit` — modify files
- `Write` — create files
- `WebSearch` — internet search
- `WebFetch` — fetch URLs
- `Agent` — spawn other agents

**Response:** `Read`

**Final toolset:** `[Read]`

---

## Step 7 — System Prompt

**Prompt:** Write the system prompt for your agent. This defines its behaviour, focus areas, and output format.

**Response:**

- **Stack:** Java 17 (javarush-project)
- **Focus areas:** naming conventions, logic errors, Java best practices, code clarity
- **No code editing** — only analysis and comments
- **Output:** `review.md` in GitHub-flavoured markdown with file path, line number, original code, issue description, suggested fix, and severity (`🔴 High` / `🟡 Medium` / `🟢 Low`)
- If no issues found: single line `✅ No issues found.`

---

## Result

Agent created successfully at:

```
.claude/agents/reviewer.md
```

### Summary

| Field | Value |
|-------|-------|
| **Name** | `reviewer` |
| **Scope** | `project` |
| **Description** | Reviews the finished diff and returns comments without editing the code |
| **Model** | `claude-sonnet-5` |
| **Effort** | `high` |
| **Tools** | `Read` |
| **Output** | `review.md` (markdown structured report) |