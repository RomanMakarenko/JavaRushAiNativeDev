# Review Decision

**Project:** `shop` — level08/task09  
**Reviewer:** Claude Code  
**Date:** 2026-07-02  
**Scope:** Documentation and config files (README.md, docs/onboarding.md, apps/web/package.json, build.gradle.kts)  
**Session commands:** `/diff`, `/review` (via Claude Code CLI)

---

## Verdict: `REQUEST_CHANGES`

The documentation contains factual errors that will cause commands to fail, internal inconsistencies between files, and a markdown syntax defect. The project scaffolding is also incomplete — both backend and frontend are missing required source files, so the documented startup commands cannot succeed.

---

## Findings

### Finding 1 (HIGH) — `docs/onboarding.md` instructs to run `npm run start`, which does not exist in `package.json`

**Evidence:**  
- `docs/onboarding.md:22` — `npm run start`  
- `apps/web/package.json:6-9` — only scripts defined are `dev`, `build`, `preview`

```bash
# Verify by inspecting the package.json:
cd src/ua/javarush/claude/code/level08/task09 && grep -A4 '"scripts"' apps/web/package.json
```

```json
"scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
}
```

**Impact:** A developer onboarding via `docs/onboarding.md` will run `npm run start` and get `missing script: start` — the command fails immediately.

**Fix:** Change `docs/onboarding.md:22` from `npm run start` to `npm run dev` (matching README.md:16 and package.json:7).

---

### Finding 2 (HIGH) — `docs/onboarding.md` and `README.md` contradict each other on the health endpoint path

**Evidence:**  
- `README.md:21` — `GET /api/health`  
- `docs/onboarding.md:13` — `GET /health`

```bash
# Cross-reference both references:
cd src/ua/javarush/claude/code/level08/task09
grep -rn 'health' README.md docs/onboarding.md
```

**Impact:** A developer following the onboarding doc and checking `GET /health` will receive a 404 if the actual endpoint is `GET /api/health`, or vice versa. This delays debugging.

**Fix:** Align both documents to the same endpoint path. Since `README.md` states `/api/health` and is the authoritative project description, `docs/onboarding.md` should match.

---

### Finding 3 (MEDIUM) — `README.md` has an unclosed fenced code block

**Evidence:**  
- `README.md:22` contains an opening `` ``` `` with no matching closing `` ``` `` before end-of-file at `README.md:23`.

```bash
# View the last lines of the file:
tail -3 src/ua/javarush/claude/code/level08/task09/README.md
```

**Impact:** Markdown renderers (GitHub, GitLab, IDEs) may display the remainder of the document as a code block, or fail to render the file correctly.

**Fix:** Either remove the stray backticks at `README.md:22` or supply the content that was meant to appear inside the block and close it with `` ``` ``.

---

### Finding 4 (MEDIUM) — No Java source files found; `./gradlew bootRun` cannot start

**Evidence:**  
- `build.gradle.kts` declares Spring Boot starter-web and Java 17.  
- No `src/main/java/` directory or `.java` files exist.

```bash
# Verify:
cd src/ua/javarush/claude/code/level08/task09
find . -name '*.java' | wc -l
# → 0 files
```

**Impact:** Running `./gradlew bootRun` (documented in `README.md:9` and `docs/onboarding.md:10`) will fail with a build error because there is no `@SpringBootApplication` entry point class.

**Recommendation:** Add the minimum Spring Boot application class before publishing documentation that claims the project is runnable.

---

### Finding 5 (MEDIUM) — `apps/web/` contains only `package.json` with no frontend source files; `npm run dev` cannot start

**Evidence:**  
- `apps/web/` contains only `package.json`.  
- No Vite config (`vite.config.js`), no `index.html`, no React/JS source files.

```bash
# Verify:
ls -la src/ua/javarush/claude/code/level08/task09/apps/web/
# → total 8 (only . and .. and package.json)
```

**Impact:** Running `npm run dev` (`README.md:17`) will fail because Vite requires at minimum a `vite.config.js` and an `index.html`.

**Recommendation:** Either scaffold the minimum frontend files, or document that the frontend scaffolding is coming soon and that `npm run dev` is not yet functional.

---

### Finding 6 (LOW) — No `.env.example` file despite it being a common expectation

**Evidence:**  
- `README.md` and `docs/onboarding.md` do not mention environment variables.  
- No `.env.example` or `.env` file exists anywhere in the project.

```bash
# Verify:
find src/ua/javarush/claude/code/level08/task09 -name '.env*'
# → no output
```

**Impact:** If the Spring Boot backend requires environment-specific config (e.g., database URL, payment service URL), new developers have no template to copy.

**Recommendation:** Add `.env.example` if any environment variables are needed, or document explicitly that the project runs with zero configuration.

---

## Summary

| # | Severity | File | Issue |
|---|----------|------|-------|
| 1 | HIGH | `docs/onboarding.md` | `npm run start` does not exist in `package.json` |
| 2 | HIGH | `docs/onboarding.md` vs `README.md` | Health endpoint paths contradict (`/health` vs `/api/health`) |
| 3 | MEDIUM | `README.md` | Unclosed fenced code block at end of file |
| 4 | MEDIUM | `build.gradle.kts` | No Java source files — `./gradlew bootRun` will fail |
| 5 | MEDIUM | `apps/web/package.json` | No frontend source files — `npm run dev` will fail |
| 6 | LOW | (project root) | No `.env.example` template |

**5 out of 6 findings are supported by executable commands; all 6 cite specific file paths and line numbers.**

*Decision generated after running `git diff HEAD~1..HEAD` (equivalent to `/diff`) and `/code-review` commands inside Claude CLI.*