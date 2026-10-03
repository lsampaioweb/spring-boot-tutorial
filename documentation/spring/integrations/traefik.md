# Traefik

Route a Spring Boot app through Traefik with a Host rule.

Working sample: [`samples/26-traefik`](../../../samples/26-traefik). Runbook:
[`samples/26-traefik/README.md`](../../../samples/26-traefik/README.md).

Infrastructure: [`samples/infrastructure/traefik/README.md`](../../../samples/infrastructure/traefik/README.md).

## Before you start

- Previous: [WebSocket](../advanced/websocket.md) — `samples/25-websocket`
- Docker Compose or Podman Compose, Java 25, Maven 3.9+
- DNS or `/etc/hosts` for `app.lan.home` (or use a `Host` header with curl)
- Time: ~25 minutes

## Why this exists

Containers rarely expose every app port on the host. Traefik terminates HTTP on
port **80** and routes by Host / labels to the service on the shared
`tutorial-network`. This sample shows labels, actuator credentials, and a simple
hello endpoint.

## What you will see

- Traefik ping on `http://127.0.0.1:8081/ping` → `OK`
- App direct (Maven, development): `GET /api/v1/users/hello` on **8080**
- Via Traefik: same path with `Host: app.lan.home` on port **80**
- Expected hello body: `{"message":"Hello from the Traefik sample."}`

## Run

### 1) Start Traefik

```bash
cd samples/infrastructure/traefik
cp .env.example .env
# Docker: comment out CONTAINER_SOCKET in .env
# Podman: keep CONTAINER_SOCKET as in .env.example
docker compose up -d
curl -fsS http://127.0.0.1:8081/ping
```

Expect `OK`. Dashboard: `http://localhost:8081/dashboard/`

Optional hosts entry:

```bash
127.0.0.1 app.lan.home
```

### 2) Run the app (Maven, direct)

```bash
cd samples/26-traefik
export SECURITY_ACTUATOR_USERNAME=actuator
export SECURITY_ACTUATOR_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### 3) Or run behind Traefik (Compose)

Production profile in the image listens on **9443** inside the container (no host
`ports:` — Traefik is ingress). Labels use `Host(\`app.lan.home\`)` and
`loadbalancer.server.port=9443`.

```bash
cd samples/26-traefik
mvn -q package
docker build --tag=lsampaioweb/app:1.0 .
export SECURITY_ACTUATOR_USERNAME=actuator
export SECURITY_ACTUATOR_PASSWORD=change-me
docker compose up -d
```

## Try it

Direct (Maven development on 8080):

```bash
curl -i http://localhost:8080/api/v1/users/hello
```

Via Traefik:

```bash
curl -i -H 'Host: app.lan.home' http://localhost/api/v1/users/hello
```

Expected: `HTTP/1.1 200` and
`{"message":"Hello from the Traefik sample."}`.

Actuator: `/actuator/health` is anonymous **200**; `/actuator/info` needs the
actuator credentials (**401** without them).

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `UserRestController` | `GET /api/v1/users/hello` |
| `docker-compose.yml` | Traefik labels + `tutorial-network` |
| `SECURITY_ACTUATOR_*` | Required actuator Basic credentials |
| Infra `docker-compose.yml` | Traefik on 80 / dashboard 8081 |

## Tests

```bash
cd samples/26-traefik && mvn test
```

Includes actuator security and Compose ingress governance tests.

## Stop

```bash
# Maven app: Ctrl+C
# Compose app:
cd samples/26-traefik && docker compose down
cd samples/infrastructure/traefik && docker compose down
```

## Next

[Tracing](tracing.md) — `samples/28-tracing`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
