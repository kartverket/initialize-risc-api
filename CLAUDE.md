# AGENTS.md

## Overview

Kotlin/Ktor microservice that generates default RiSc (Risk Scorecard) documents. Fetches default RiSc templates from Airtable, merges them with user-provided initial RiSc content, and returns the result. Part of the `ros-as-code` system at Kartverket.

## Tech stack

- **Kotlin 2.2 / JVM 23** (Java toolchain 24), **Ktor 3.3** with Netty
- **Gradle 9.3** (wrapper), **ktlint 1.6** via `org.jlleitschuh.gradle.ktlint` plugin
- **kotlinx.serialization** for JSON (not Jackson)
- **ktor-openapi / ktor-swagger-ui** for API docs at `/swagger` and `/api.json`
- Tests use **JUnit 5**, **MockK**, and **ktor-server-test-host**

## Commands

```bash
# Build (includes ktlint check + tests)
./gradlew build

# Tests only
./gradlew test

# Lint check / auto-format
./gradlew ktlintCheck
./gradlew ktlintFormat

# Run locally via Docker (requires .env with Airtable secrets)
cp .env.example .env   # fill in values first
docker compose up --build

# Health check
curl http://localhost:8085/health
```

CI runs `./gradlew build` (which includes ktlint + tests) then builds a Docker image. There is no separate lint CI step -- it's all in `build`.

## Project structure

```
src/main/kotlin/kartverket/no/
  Application.kt          # Ktor entry point, config loading, module setup
  config/AppConfig.kt     # Singleton config objects (AirTableConfig, GenerateRiScConfig)
  plugins/Routing.kt      # Top-level route wiring: /health, /swagger, /generate, /descriptors
  generate/               # POST /generate -- core business logic
  descriptor/             # GET /descriptors -- lists available RiSc descriptors
  airTable/               # Airtable HTTP client
  exception/              # Custom exception types
  utils/                  # Validators
src/main/resources/
  application.yaml        # Ktor config; env vars resolved with $VAR_NAME syntax
```

## Key conventions

- **Config via `application.yaml`**: Env vars like `$AIRTABLE_BASE_ID` are resolved by Ktor's config system, not `System.getenv()`. Config is loaded into singleton objects in `AppConfig`.
- **No dependency injection framework**: Services and config are Kotlin `object` singletons.
- **ktlint enforced**: The build will fail if code doesn't conform. Run `./gradlew ktlintFormat` before committing.
- **PR template is in Norwegian**: The team uses Norwegian for PR descriptions and some code comments.
- **Rebase preferred over merge**: PR checklist asks contributors to rebase on `main`.

## Environment

The app requires these env vars at runtime (set in `.env` for Docker Compose):

| Variable | Purpose |
|---|---|
| `AIRTABLE_BASE_ID` | Airtable base identifier |
| `AIRTABLE_API_TOKEN` | Airtable API authentication |
| `AIRTABLE_TABLE_ID` | Airtable table identifier |

Never commit `.env` -- it is gitignored.

## Testing notes

- Tests mock `AirTableClientService` (an `object`) using `mockkObject`. No real Airtable calls in tests.
- Route tests use Ktor's `testApplication` with manually installed plugins (not the full `Application.module()`).
- Config singletons are set up in `@BeforeEach` with dummy values -- no `.env` file needed for tests.
