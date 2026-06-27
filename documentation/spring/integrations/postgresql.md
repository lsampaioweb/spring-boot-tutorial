# Spring Boot + PostgreSQL

This page explains how to run PostgreSQL infrastructure and connect it to the sample application in `samples/19-postgres`.

## Prerequisites
1. Java 25
1. Maven 3.9+
1. Podman with Podman Compose

Before first use with rootless Podman:

```bash
systemctl --user enable --now podman.socket
podman system migrate
```

## 1. Start PostgreSQL Infrastructure

```bash
cd samples/infrastructure/postgres
podman compose up -d
```

Check container health:

```bash
podman compose ps
```

## 2. Run the Spring Boot Sample

Open a new terminal:

```bash
cd samples/19-postgres
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=tutorial
export DB_USER=tutorial
export DB_PASSWORD=tutorial
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

The sample uses `JdbcClient` and reads datasource values from those `DB_*` variables.

## 3. Test Endpoints

Create one user:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"Alice","email":"alice@example.com"}' http://localhost:8080/api/v1/users
```

List users:

```bash
curl http://localhost:8080/api/v1/users
```

## 4. Stop Infrastructure

```bash
cd samples/infrastructure/postgres
podman compose down
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
