# 17 — Container

Containerized Spring Boot hello API with Dockerfile and Compose.

## Before you start

- Previous: [16-virtual-threads](../16-virtual-threads/README.md)
- Topic: [documentation/spring/extra/container.md](../../documentation/spring/extra/container.md)
- Docker or Podman

## Run locally (no container)

```bash
cd samples/17-container
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

```bash
curl -i http://localhost:8080/api/v1/users/hello
```

## Run with Compose

```bash
cd samples/17-container
docker network create spring-boot-container-network 2>/dev/null || true
mvn -q package -DskipTests
docker build --tag=lsampaioweb/spring-boot-container:1.0 .
docker compose up -d
```

```bash
curl -i http://localhost:8080/api/v1/users/hello
```

Expected: `HTTP/1.1 200` with a hello JSON payload.

## Stop

```bash
docker compose down
```

## Tests

```bash
mvn test
```

## Next

[18-security](../18-security/README.md)
