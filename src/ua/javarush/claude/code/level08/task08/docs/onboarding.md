# Onboarding — example-shop

## Project structure

```
.
├── build.gradle.kts              # Backend: Spring Boot + Gradle
├── .env.example                  # Environment variables template
├── apps/
│   └── web/
│       └── package.json          # Frontend: Vite + React
├── src/
│   └── main/
│       └── resources/
│           └── application.yml   # Spring Boot config
└── docs/
    └── onboarding.md             # This file
```

---

## Prerequisites

- **Java 17** — set via `sourceCompatibility = JavaVersion.VERSION_17` in `build.gradle.kts`
- **Node.js** — version compatible with Vite 5 (defined in `apps/web/package.json`)
- **Gradle** — the project uses Gradle via `build.gradle.kts` (Gradle Wrapper expected)

---

## Environment variables

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

| Variable | Default | Description |
|----------|---------|-------------|
| `PAYMENTS_BASE_URL` | `http://localhost:9090` | Payment service base URL |

Only `.env.example` is committed — real secrets stay local.

---

## How to run

**Backend** (Spring Boot, from project root):

```bash
./gradlew bootRun
```

The server starts on port `8080` (`server.port` in `application.yml`).

**Frontend** (Vite + React, inside `apps/web/`):

```bash
cd apps/web
npm install
npm run start:dev
```

Available frontend scripts (from `apps/web/package.json`):

| Command | Description |
|---------|-------------|
| `npm run start:dev` | Start Vite dev server |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run lint` | Run ESLint |

---

## Assumptions

- The payment service at `PAYMENTS_BASE_URL` (`http://localhost:9090` by default) is expected to be running externally — this project does not include or set up that service.
- The backend and frontend are independent processes; no reverse proxy or orchestrator config is present in the repository.
- Gradle Wrapper is assumed to be available (not checked into this project's visible files).

---

## Limitations

- Only `spring-boot-starter-web` and `spring-boot-starter-test` are declared as dependencies — no database, security, or messaging starters are included.
- The `apps/web/` directory contains only `package.json`; no Vite config, React source files, or ESLint config are present in the repository yet.
- No Docker, docker-compose, or deployment configuration is included.
- The application name is hardcoded as `example-shop` in `application.yml`.