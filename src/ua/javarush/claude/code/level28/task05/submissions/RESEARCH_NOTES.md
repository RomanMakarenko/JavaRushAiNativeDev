# Research Notes

## Confirmed

- **Java baseline:** Spring Boot 3.x requires Java 17 or newer; the release notes confirm the 17+ baseline and recommend Java 21 for long-term support. The project currently declares Java 8 (`VERSION_1_8`), so the build configuration is below the documented baseline. [Docs: Migration Guide — Java baseline](../official-docs/spring-boot-3-migration-guide.md#java-baseline) · [Docs: Release Notes — Java](../official-docs/release-notes-3.3.md#java) · [File: `build.gradle`](../build.gradle)
- **Jakarta namespace migration:** Spring Boot 3.x requires replacing `javax.persistence.*` with `jakarta.persistence.*`; `Subscription` still imports `javax.persistence.Entity` and `javax.persistence.Id`. [Docs: Migration Guide — Jakarta EE 9 (`javax -> jakarta`)](../official-docs/spring-boot-3-migration-guide.md#jakarta-ee-9-javax---jakarta) · [Docs: Release Notes — Jakarta](../official-docs/release-notes-3.3.md#jakarta) · [File: `src/main/java/com/acme/cashflow/subscriptions/Subscription.java`](../src/main/java/com/acme/cashflow/subscriptions/Subscription.java)
- **Security configuration migration:** `WebSecurityConfigurerAdapter` is removed in Spring Security 6 / Spring Boot 3.x; the documented replacement is a component-based `SecurityFilterChain` bean. `SecurityConfig` extends the removed adapter and overrides `configure(HttpSecurity)`. [Docs: Migration Guide — Spring Security](../official-docs/spring-boot-3-migration-guide.md#spring-security) · [File: `src/main/java/com/acme/cashflow/security/SecurityConfig.java`](../src/main/java/com/acme/cashflow/security/SecurityConfig.java)
- **CSRF DSL style:** The migration guide identifies `http.csrf().disable()` and similar older DSL chains as requiring rewriting to the new style; `SecurityConfig` uses that exact older call. [Docs: Migration Guide — Spring Security](../official-docs/spring-boot-3-migration-guide.md#spring-security) · [File: `src/main/java/com/acme/cashflow/security/SecurityConfig.java`](../src/main/java/com/acme/cashflow/security/SecurityConfig.java)
- **Configuration-property scope:** The release notes state that deprecated configuration keys log startup warnings, while the migration guide gives `spring.redis.*` → `spring.data.redis.*` as an example. No application configuration file is present among the reviewed files, so there is no confirmed project-specific key usage to map. [Docs: Migration Guide — Configuration properties](../official-docs/spring-boot-3-migration-guide.md#configuration-properties) · [Docs: Release Notes — Configuration](../official-docs/release-notes-3.3.md#configuration) · [File scope reviewed: `src/`](../src/)

## Assumptions

- The intended target is Spring Boot 3.x, consistent with the migration guide and the requested research scope; no application or official-doc changes are made in this notes-only task.
- The two Java files under `src/main/java/` are the complete application source currently relevant to this task, because they are the only files present under `src/`.
- The `javax.persistence` imports and `WebSecurityConfigurerAdapter` usage are migration markers intentionally left in place for a later implementation task.
- The exact Spring Boot 3.x minor version and corresponding dependency-management/plugin versions have not yet been selected.

## Unknowns

- Whether the project has runtime configuration outside this directory (for example, external environment variables or deployment-managed properties) that uses deprecated configuration keys.
- Whether additional tests, resources, controllers, repositories, or generated sources exist outside the reviewed `src/` tree.
- Which Spring Security 6 authorization rules and CSRF policy the application requires beyond the currently visible `csrf().disable()` call.
- Whether the project should target Java 17 or Java 21 after migration; the docs establish 17 as the minimum and recommend 21, but do not select the project target.
- Whether the dependency graph needs explicit Jakarta or Spring Security adjustments after the Spring Boot plugin is upgraded, since no dependency resolution or build migration was run.