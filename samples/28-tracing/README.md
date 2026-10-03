# 28 — Tracing (Micrometer + OpenTelemetry)

Cross-JVM tracing demo with **Micrometer Tracing**, the **OpenTelemetry** bridge, and **OTLP** export to the tutorial Collector. Correlation IDs (`traceId` / `spanId`) appear only in this sample’s Logback patterns.

## Layout

| Path | Role |
| --- | --- |
| `callee/` | Receives `GET /api/v1/pings` (port `8081`) |
| `caller/` | Starts the demo at `GET /api/v1/traces/demo` and calls the callee via `RestClient` (port `8080`) |
| `../infrastructure/opentelemetry/` | OpenTelemetry Collector (OTLP → stdout) |

## Prerequisites

1. Java 25 and Maven.
1. Collector running — see [`samples/infrastructure/opentelemetry/README.md`](../infrastructure/opentelemetry/README.md).

```bash
cd samples/infrastructure/opentelemetry
docker compose up -d
```

## Configuration

| Variable | Purpose |
| --- | --- |
| `TRACING_USER_PASSWORD` | Shared HTTP Basic password for caller API, callee API, and caller→callee calls |
| `TRACING_USER_USERNAME` | Optional username override (default `tracing-user`) |

## Run

Terminal 1 — callee:

```bash
cd samples/28-tracing/callee
export TRACING_USER_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Terminal 2 — caller:

```bash
cd samples/28-tracing/caller
export TRACING_USER_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Or use the VS Code / Cursor launch configs `28-tracing-callee` and `28-tracing-caller`.

## Access

- Caller demo: `http://localhost:8080/api/v1/traces/demo` (HTTP Basic)
- Callee ping: `http://localhost:8081/api/v1/pings` (HTTP Basic)
- Health: `http://localhost:8080/actuator/health`, `http://localhost:8081/actuator/health`
- Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`, `http://localhost:8081/swagger-ui/index.html`

## Verify

1. Health:

```bash
curl -s http://localhost:8081/actuator/health
curl -s http://localhost:8080/actuator/health
```

2. Demo request:

```bash
curl -s -u tracing-user:change-me http://localhost:8080/api/v1/traces/demo
```

Example body:

```json
{"caller":"tracing-caller","callee":"tracing-callee","timestamp":"..."}
```

3. Same `traceId` in both app logs (`logs/tracing-caller.log` and `logs/tracing-callee.log`, or console in the `development` profile).

4. Spans in Collector stdout:

```bash
cd samples/infrastructure/opentelemetry
docker compose logs -f otel-collector
```

## Tests

```bash
cd samples/28-tracing/callee && mvn test
cd samples/28-tracing/caller && mvn test
```

## What this sample teaches

- Boot’s supported path: Micrometer Observation → OpenTelemetry → OTLP
- W3C `traceparent` propagation across two JVMs via `RestClient`
- HTTP Basic on private APIs while still propagating trace context
- Log correlation with MDC `traceId` / `spanId`
- Always-on sampling for demos (`management.tracing.sampling.probability=1.0`)

## Out of scope

Jaeger/Tempo UI, Brave, JDBC/messaging instrumentation, structured JSON log formats.
