# Container

Working sample: [`samples/17-container`](../../../samples/17-container). Runbook:
[`samples/17-container/README.md`](../../../samples/17-container/README.md).

Package a Spring Boot app as a Docker image and run it with Compose.

## Before you start

- Previous: [Virtual Threads](../advanced/virtual-threads.md)
- Docker or Podman ([docker.md](docker.md))
- Java 25, Maven 3.9+

## Why this exists

Running the same jar in a container matches how many teams deploy services.
This sample shows a hardened Compose service and a simple hello endpoint.

## What you will see

- `GET /api/v1/users/hello` returns JSON on port `8080`

## Run locally (no container)

```bash
cd samples/17-container
mvn spring-boot:run -Dspring-boot.run.profiles=development
curl -i http://localhost:8080/api/v1/users/hello
```

## Run with Docker / Compose

Create the **external** network declared in the sample Compose file:

```bash
docker network create spring-boot-container-network
```

Build and start (from `samples/17-container`):

```bash
mvn -q package -DskipTests
docker build --tag=lsampaioweb/spring-boot-container:1.0 .
docker compose up -d
```

Image name and tag must match
[`docker-compose.yml`](../../../samples/17-container/docker-compose.yml)
(`lsampaioweb/spring-boot-container:1.0`).

### Try it

```bash
curl -i http://localhost:8080/api/v1/users/hello
```

Expected: `HTTP/1.1 200` with a hello payload.

### Logs and shell

```bash
docker compose logs -f spring-boot-container
docker exec -it spring-boot-container sh
```

### Stop

```bash
docker compose down
```

## Notes

- Dockerfile: [`samples/17-container/Dockerfile`](../../../samples/17-container/Dockerfile)
- The image runs as user `app` (UID `1112`). If bind-mounted logs fail with
  permission errors: `sudo chown -R 1112:1112 ./logs`
- Compose uses logging driver `k8s-file` (common with Podman). On Docker Engine,
  if that driver is missing, comment out the `logging:` block in
  `docker-compose.yml` or switch to the default `json-file` driver.

## Previous

[Virtual Threads](../advanced/virtual-threads.md).

## Next
[Security](../advanced/security.md) — `samples/18-security`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
