# Actuator

Expose health, info, and metrics under `/actuator` without a domain REST API.

Working sample: [`samples/07-actuator`](../../../samples/07-actuator). Runbook:
[`samples/07-actuator/README.md`](../../../samples/07-actuator/README.md).

## Before you start

- Previous: [i18n](../intermediate/i18n.md) — `samples/06-i18n`
- Java 25, Maven 3.9+
- Time: ~10 minutes

## Why this exists

Operators and orchestrators need a standard health signal. Actuator adds
management endpoints separately from your business API. This sample exposes
`health`, `info`, and `metrics` only.

## What you will see

- App listens on **8080** with the `development` profile
- `GET /actuator/health` returns JSON with `"status":"UP"`
- Startup log mentions exposing endpoints under `/actuator`

## Run

```bash
cd samples/07-actuator
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Base exposure (see `application.yml`):

```yml
management:
  endpoints:
    web:
      exposure:
        include: "health,info,metrics"
  endpoint:
    health:
      probes:
        enabled: true
```

Development sets `management.endpoint.health.show-details: always` and
`server.port: 8080`. Default production uses port **9443** and hides details.

## Try it

```bash
curl -i http://localhost:8080/actuator/health
```

Expected: `HTTP/1.1 200`, `Content-Type` includes
`application/vnd.spring-boot.actuator.v3+json`, and a body like:

```json
{
  "status": "UP",
  "groups": ["liveness", "readiness"],
  "components": {
    "diskSpace": { "status": "UP" },
    "livenessState": { "status": "UP" },
    "ping": { "status": "UP" },
    "readinessState": { "status": "UP" },
    "ssl": { "status": "UP" }
  }
}
```

Exact component keys can vary slightly by Boot version; `"status":"UP"` and the
`groups` array are the stable signals for this lesson.

Optional:

```bash
curl -i http://localhost:8080/actuator/info
curl -i http://localhost:8080/actuator/metrics
```

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `ActuatorApplication` | Entry point (no domain controllers) |
| `pom.xml` | `spring-boot-starter-web` + `spring-boot-starter-actuator` |
| `application.yml` / profile YAML | Exposure, probes, ports, health details |

## Tests

```bash
cd samples/07-actuator && mvn test
```

Loads the Spring context.

## Stop

`Ctrl+C`.

## Previous

[i18n](../intermediate/i18n.md).

## Next
[REST](rest.md) — `samples/08-restapi`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
