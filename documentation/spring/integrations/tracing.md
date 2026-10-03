# Spring Boot + Tracing (Micrometer + OpenTelemetry)

Working sample: `samples/28-tracing`. Infrastructure: `samples/infrastructure/opentelemetry`.

The hands-on runbook (start Collector, run caller/callee, verify `traceId`, read Collector logs) lives next to the sample: [`samples/28-tracing/README.md`](../../../samples/28-tracing/README.md). Compose for the Collector: [`samples/infrastructure/opentelemetry/README.md`](../../../samples/infrastructure/opentelemetry/README.md).

## What the sample demonstrates

| Piece | Role |
| --- | --- |
| `callee` | `GET /api/v1/pings` on port `8081` |
| `caller` | `GET /api/v1/traces/demo` on port `8080`; calls callee with `RestClient` |
| OpenTelemetry Collector | OTLP receiver; prints spans to stdout |

- Boot path: Micrometer Observation → OpenTelemetry → OTLP
- W3C `traceparent` propagation across two JVMs
- HTTP Basic on private APIs; caller sends the same credentials on outbound calls
- Log correlation with MDC `traceId` / `spanId` (this sample only)
- Demo sampling: `management.tracing.sampling.probability=1.0`
- Metrics export to the Collector is disabled when the Collector is traces-only (`management.otlp.metrics.export.enabled=false`)

## Out of scope

Jaeger/Tempo UI, Brave/Zipkin, JDBC/messaging instrumentation, JSON log formats.

## Run / Try it

Follow [`samples/28-tracing/README.md`](../../../samples/28-tracing/README.md):

1. Start the Collector
1. Start callee (`8081`) then caller (`8080`) with `TRACING_USER_PASSWORD`
1. `curl` the caller demo endpoint
1. Confirm the same `traceId` in caller logs, callee logs, and Collector stdout

## Previous

[Async](../intermediate/async.md).

## Next
Catalog complete for integrations — return to the [root README](../../../README.md).

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
