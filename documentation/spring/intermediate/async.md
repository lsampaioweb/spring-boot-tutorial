# Async

Accept a job with `@Async` workers and poll status (HTTP Basic auth).

Working sample: [`samples/27-async/basics`](../../../samples/27-async/basics).
Runbook: [`samples/27-async/basics/README.md`](../../../samples/27-async/basics/README.md).

## Before you start

- Previous: [Events](events.md) — `samples/15-events`
- Java 25, Maven 3.9+
- Time: ~20 minutes

## Why this exists

Events stay in-process fire-and-forget. This sample exposes an **accept-and-poll**
HTTP API: `POST` returns **202** immediately; a background `@Async` worker updates
job status for later `GET`. Basic auth protects the job endpoints.

## What you will see

- Required env: `ASYNC_USER_PASSWORD`
- Default username: `async-user` (`ASYNC_USER_USERNAME` optional)
- `POST /api/v1/jobs` → **202** + `Location` + `"status":"QUEUED"`
- Poll until `"status":"SUCCEEDED"` (or `"FAILED"`)

## Run

```bash
cd samples/27-async/basics
export ASYNC_USER_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`. Swagger: `http://localhost:8080/swagger-ui/index.html`

## Try it

Submit:

```bash
curl -i -u async-user:change-me -X POST http://localhost:8080/api/v1/jobs \
  -H 'Content-Type: application/json' \
  -d '{"input":"demo","failForDemo":false,"delayMs":200}'
```

Expected: `HTTP/1.1 202`, `Location: .../api/v1/jobs/<uuid>`, body includes
`"status":"QUEUED"`.

Poll (replace `<id>` from `Location` or JSON `id`):

```bash
curl -i -u async-user:change-me http://localhost:8080/api/v1/jobs/<id>
```

Expected after ~200ms: `HTTP/1.1 200` with `"status":"SUCCEEDED"` and
`"result":"DEMO"` (input uppercased).

Demo failure path: set `"failForDemo":true` → terminal `"status":"FAILED"`,
`"failureCode":"JOB_PROCESSING_FAILED"`.

Unauthenticated calls to the job API return **401**.

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `AsyncJobRestController` | 202 create + GET status |
| `AsyncJobServiceImpl` | Job store / orchestration |
| `AsyncJobWorker` | `@Async` processing |
| `SecurityConfig` / `SecurityProperties` | Basic auth; binds `ASYNC_USER_*` |

## Tests

```bash
cd samples/27-async/basics && mvn test
```

Controller, lifecycle, and service tests activate the `development` profile and
supply credentials through `@SpringBootTest(properties = { ... })`
(`async-test-user` / `async-test-password`).

## Stop

`Ctrl+C`.

## Previous

[Traefik](../integrations/traefik.md).

## Next
[Virtual Threads](../advanced/virtual-threads.md) — `samples/16-virtual-threads`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
