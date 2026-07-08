<!-- filepath: samples/23-geography/README.md -->
# Geography API

## Overview

REST API for managing geographic data: countries, states, and cities.

## Prerequisites

- Java 25
- Maven 3.9+
- PostgreSQL 16+

## Running locally

```bash
export DB_USER=your_user
export DB_PASSWORD=your_password
mvn spring-boot:run
```

## Endpoints

| Method | Path                    | Description           |
|--------|-------------------------|-----------------------|
| GET    | /api/v1/countries       | List all countries    |
| GET    | /api/v1/countries/{id}  | Get country by ID     |
| POST   | /api/v1/countries       | Create country        |
| PUT    | /api/v1/countries/{id}  | Update country        |
| DELETE | /api/v1/countries/{id}  | Delete country        |
| -      | -                       | -                     |
| GET    | /api/v1/states          | List all states       |
| GET    | /api/v1/states/{id}     | Get state by ID       |
| POST   | /api/v1/states          | Create state          |
| PUT    | /api/v1/states/{id}     | Update state          |
| DELETE | /api/v1/states/{id}     | Delete state          |
| -      | -                       | -                     |
| GET    | /api/v1/cities          | List all cities       |
| GET    | /api/v1/cities/{id}     | Get city by ID        |
| POST   | /api/v1/cities          | Create city           |
| PUT    | /api/v1/cities/{id}     | Update city           |
| DELETE | /api/v1/cities/{id}     | Delete city           |

---

## Review Findings — TODO

Findings from the code review, ordered by recommended fix priority.
Each item references the finding ID from the review report.

### Critical

- [ ] **C-02** — Add Spring Security (deny-by-default; no auth/authz on any endpoint)
- [ ] **C-01** — Add behavioral test coverage (zero tests for controllers, services, repositories)

### High

- [ ] **H-01** — Fix `handleAppException` — returns HTTP 200 for all domain exceptions (404, etc.)
- [ ] **H-03** — Fix test profile — `@SpringBootTest` with `"development"` requires a live database in CI
- [ ] **H-04** — Add `@WebMvcTest` controller tests (all 15 endpoints unverified)
- [ ] **H-05** — Add `@JdbcTest` repository tests (all SQL + `GeneratedKeyHolder` NPE risk unverified)
- [ ] **H-06** — Add service unit tests (Mockito; null-check and exception-throwing paths unverified)
- [ ] **H-07** — Add pagination to all three `findAll` endpoints (unbounded queries)
- [ ] **H-08** — Add HTTPS + TLS in production; change production port from 8080 to 9443
- [ ] **H-09** — Protect Actuator endpoints (health details exposed to unauthenticated callers)
- [ ] **H-02** — Migrate manual mappers to MapStruct (`*DtoMapper` interfaces, `unmappedTargetPolicy = ERROR`)
- [ ] **H-10** — Fix `contextLoads()` — add assertion and rename per naming convention

### Medium

- [ ] **M-01** — Replace all `var` with explicit types in service impls and locale config
- [ ] **M-02** — Resolve validation messages through `MessageSource` (raw i18n keys returned to clients)
- [ ] **M-03** — Remove domain exception throws from repositories (`update`, `deleteById`) — service layer owns this
- [ ] **M-04** — Add `Location` header to all `POST 201 Created` responses
- [ ] **M-05** — Change `ErrorResponse.timestamp` from `LocalDateTime` to `OffsetDateTime`
- [ ] **M-06** — Fix `messages_pt_BR.properties` — replace Unicode escapes with literal UTF-8 characters
- [ ] **M-07** — Fix `handleNoResourceFoundException` — resolve message via `MessageSource` instead of `ex.getMessage()`
- [ ] **M-08** — Add `metrics` to Actuator exposure list (`health,info,metrics`)
- [ ] **M-09** — Fix import order — `jakarta.*` must be placed before third-party groups
- [ ] **M-10** — Add `@Slf4j` + error logging to `GlobalExceptionHandler.handleGenericError`
- [ ] **M-11** — Create `src/test/resources/application-test.yml` with test-scoped datasource
- [ ] **M-12** — Add `webEnvironment = NONE` to smoke test; prefer slice tests for behavior
- [ ] **M-13** — Remove redundant `findById` pre-check in `delete()` service methods (double round-trip)
- [ ] **M-14** — Downgrade `findAll`/`findById` log calls in repositories to `DEBUG` level
- [ ] **M-15** — Tune HikariCP: raise `minimum-idle` to 5, lower `connection-timeout` to 5000 ms
- [ ] **M-16** — Add `server.error.include-stacktrace: "never"` to base `application.yml`
- [ ] **M-17** — Add `@Size(max=…)` constraints on all free-text input fields
- [ ] **M-18** — Disable `/v3/api-docs` in production (`springdoc.api-docs.enabled: false`)
- [ ] **M-19** — Rename test method to `{method}_when{Condition}_should{Outcome}` convention

### Low

- [ ] **L-01** — Move `DatabaseException` to `exception` package
- [ ] **L-02** — Remove unused `@Slf4j` from service impls (or add meaningful log statements)
- [ ] **L-03** — README: add `<!-- filepath: ... -->` header; rename sections *(this item — already done)*
- [ ] **L-04** — Create `.vscode/launch.json` with correct `cwd` for IDE logback path resolution
- [ ] **L-05** — Add null guard on `keyHolder.getKey()` in all three `insert()` repository methods
- [ ] **L-06** — Narrow `DatabaseException` visibility from `public` to package-private

---

## Architecture Topics — To Discuss

These are architectural gaps or design decisions that do not yet have instruction files or sample code.
Each item may result in a new instruction file, a new sample, or a change to existing code.

### Your items

- [ ] **D-01** — **Repository interfaces** — Repositories are concrete `@Repository` classes; they cannot be mocked in service unit tests without Spring context, and switching the persistence backend (JDBC → MyBatis, in-memory, etc.) requires changing the service. Define a repository interface per entity (`CountryRepository`) and rename the current class to `CountryRepositoryImpl`. This mirrors the service interface+impl pattern already in place. *(Connects to H-06: service unit tests currently cannot stub repositories)*

- [ ] **D-02** — **Pluggable log appenders** — The current `logback-spring.xml` hard-codes file and console appenders. Switching to Loki, Datadog, or a DB appender means editing XML. The SLF4J / Logback abstraction already supports this via `<appender>` swap, but the procedure is undocumented and no instruction file exists. Needs: appender swap guide, structured JSON logging option (`logstash-logback-encoder`), and MDC fields (correlation ID, user ID) so every backend receives the same context. *(See also D-06)*

- [ ] **D-03** — **Pagination instruction file** — H-07 fixes unbounded `findAll` queries, but there is no project-wide instruction file defining: `PageRequest` / `Pageable` usage, `PagedResponse<T>` wrapper record shape, default page size, max page size cap, SQL `LIMIT`/`OFFSET` vs keyset pagination, and `Link` header (RFC 5988) convention. Without an instruction file, different features implement pagination differently.

- [ ] **D-04** — **Enum conventions** — No instruction file exists for: naming (`UPPER_SNAKE_CASE`), DB storage strategy (store as `VARCHAR` name vs. ordinal integer; `VARCHAR` preferred for readability and schema stability), `@JsonProperty` / `@JsonValue` for API serialization, OpenAPI documentation of allowed values via `@Schema(enumAsRef = true)`, Bean Validation of incoming enum strings, and migration safety when enum values are added or removed.

- [ ] **D-05** — **Database type sizing and naming conventions** — No instruction file defines: choosing the smallest sufficient integer type (`SMALLSERIAL` for bounded reference tables like countries ≤ 250 rows, `SERIAL` for states/cities, `BIGSERIAL` only for high-volume transactional tables); `VARCHAR(N)` length aligned with domain rules; `CHAR(N)` only for truly fixed-length values (ISO codes); table names in `snake_case` plural; column names in `snake_case`; constraint naming (`fk_<table>_<column>`, `idx_<table>_<column>`, `uq_<table>_<column>`); foreign key naming; `NOT NULL` as the default with `NULL` as the explicit exception.

### Suggested additions

- [ ] **D-06** — **Correlation / trace ID in logs** — No MDC (Mapped Diagnostic Context) setup exists. Every incoming HTTP request should carry a `X-Correlation-ID` header (generated if absent) that is injected into the MDC and appended to every log line for that request. This is foundational for Loki, Datadog, and any distributed tracing tool. Needs: a `CorrelationIdFilter` (`OncePerRequestFilter`), MDC key convention, and logback pattern update.

- [ ] **D-07** — **Database migrations (Flyway / Liquibase)** — The current `schema.sql` is a `CREATE TABLE IF NOT EXISTS` script that runs at startup but has no versioning. In production, schema changes must be applied incrementally and tracked. Needs: Flyway or Liquibase dependency choice, migration file naming convention (`V<version>__<description>.sql`), rollback strategy, and a `test` profile that runs migrations against an embedded database.

- [ ] **D-08** — **Soft delete pattern** — The current `DELETE` endpoints issue `DELETE FROM ...` (hard delete). For reference data like countries, states, and cities — which are referenced by foreign keys — a hard delete of a parent row will either fail (FK violation) or cascade-delete children. A soft-delete pattern (`deleted_at TIMESTAMP`, `is_active BOOLEAN`) preserves referential integrity and provides an audit trail. Needs: instruction file covering the column convention, `findAll` filter to exclude deleted rows, and the behaviour difference between hard and soft delete endpoints.

- [ ] **D-09** — **Cascading delete / referential integrity rules** — Countries → States → Cities form a strict FK chain. There is no `ON DELETE` rule defined in `schema.sql`. Deleting a country that has states will fail at the DB level with an FK violation and surface as an unhandled 500. Needs: a decision on `ON DELETE RESTRICT` (explicit 409 Conflict), `ON DELETE CASCADE`, or application-level guard; an instruction file or pattern for FK violation error handling in `GlobalExceptionHandler`.

- [ ] **D-10** — **Structured `errorCode` in `ErrorResponse`** — `ErrorResponse` carries `status` (integer) and `message` (string) but no machine-readable `errorCode` (e.g., `COUNTRY_NOT_FOUND`, `VALIDATION_FAILED`). Without it, API clients must parse the message string to distinguish error types programmatically. Needs: an `errorCode` field convention, a catalogue of codes per domain exception, and OpenAPI documentation.

- [ ] **D-11** — **Caching for reference data** — Countries, states, and cities are read-heavy and change infrequently. Adding `@Cacheable` (Spring Cache with a Caffeine or Redis backend) to `findAll` and `findById` would eliminate repeated DB round-trips. Needs: instruction file covering cache naming convention, TTL policy, cache invalidation on write operations (`@CacheEvict`), and the `spring-boot-starter-cache` dependency setup.

- [ ] **D-12** — **API versioning evolution strategy** — All endpoints are prefixed with `/api/v1/`. There is no instruction file defining: when to bump to `v2`, how to co-host `v1` and `v2` simultaneously, deprecation headers (`Sunset`, `Deprecation`), and how version-specific DTOs are managed without duplicating controller code.
