# HTTP Client

Call another Spring Boot API with `RestClient` (upstream on 8080, client on 8081).

Working sample: [`samples/12-http-client`](../../../samples/12-http-client). Runbook:
[`samples/12-http-client/README.md`](../../../samples/12-http-client/README.md).

Upstream: [`samples/08-restapi`](../../../samples/08-restapi) —
[`README`](../../../samples/08-restapi/README.md).

## Before you start

- Previous: [MapStruct](mapstruct.md) — `samples/11-mapstruct`
- Java 25, Maven 3.9+
- Free ports **8080** (upstream) and **8081** (client)
- Time: ~20 minutes

## Why this exists

Services often need outbound HTTP. This sample configures a `RestClient` bean
against `external.api.base-url` and re-exposes a Users API that proxies to
`08-restapi`.

## What you will see

- Terminal 1: `08-restapi` on **8080**
- Terminal 2: `12-http-client` on **8081**
- Client `GET /api/v1/users/1` returns the same user JSON as the upstream

## Run

Terminal 1 — upstream:

```bash
cd samples/08-restapi
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Terminal 2 — client:

```bash
cd samples/12-http-client
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Client YAML (defaults):

```yml
external:
  api:
    base-url: "http://localhost:8080/api/v1"
    users: "${external.api.base-url}/users"
```

## Try it

```bash
curl -i http://localhost:8081/api/v1/users/1
```

Expected: `HTTP/1.1 200` and
`{"id":1,"name":"user-01","email":"user-01@example.com"}` fetched from upstream
`8080`.

Optional check that upstream is alive:

```bash
curl -i http://localhost:8080/api/v1/users/1
```

Swagger for the client (development): `http://localhost:8081/swagger-ui/index.html`

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `HttpClientConfiguration` | Builds `RestClient` with base URL + timeouts |
| `ExternalApiProperties` | Binds `external.api.*` |
| `UserRepositoryImpl` | Outbound HTTP calls |
| `UserRestController` / `UserServiceImpl` | Local API that delegates to the repository |

## Tests

```bash
cd samples/12-http-client && mvn test
```

Context load + i18n consistency (no live WireMock in the default suite).

## Stop

`Ctrl+C` in both terminals.

## Next

[Thymeleaf](../basic/thymeleaf.md) — `samples/13-thymeleaf`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
