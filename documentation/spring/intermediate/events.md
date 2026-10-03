# Events

Publish an in-process Spring application event and handle it in an async listener.

Working sample: [`samples/15-events`](../../../samples/15-events). Runbook:
[`samples/15-events/README.md`](../../../samples/15-events/README.md).

## Before you start

- Previous: [HTTPS](https.md) — `samples/14-https`
- Java 25, Maven 3.9+
- Time: ~15 minutes

## Why this exists

Sometimes work should continue after the HTTP response is accepted — audit,
notifications, side effects — without a message broker yet. Spring’s
`ApplicationEventPublisher` keeps that coupling inside one JVM. For broker-based
messaging, see RabbitMQ later in the catalog.

## What you will see

- `POST /api/v1/messages` → **202 Accepted**
- JSON body with `sender` and `status: "accepted"`
- Console audit log from the async listener

## Run

```bash
cd samples/15-events
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`. Swagger: `http://localhost:8080/swagger-ui/index.html`

## Try it

```bash
curl -i -X POST http://localhost:8080/api/v1/messages \
  -H 'Content-Type: application/json' \
  -d '{"sender":"ada","content":"hello"}'
```

Expected: `HTTP/1.1 202` and a body like
`{"sender":"ada","status":"accepted"}`.

Expected log (English): a line such as
`Audit recorded for sender=ada, contentLength=5, publishedAt=...`.

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `MessageRestController` | Accepts the POST, returns 202 |
| `MessageServiceImpl` / publisher | Publishes the domain event |
| `MessagePublishedEvent` | Event payload |
| `MessageAuditListener` | Async `@EventListener` audit log |

## Tests

```bash
cd samples/15-events && mvn test
```

Context load + i18n consistency.

## Stop

`Ctrl+C`.

## Next

[Async](async.md) — `samples/27-async/basics`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
