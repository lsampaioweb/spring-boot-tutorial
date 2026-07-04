# Spring Boot + PostgreSQL

This page explains how to run PostgreSQL infrastructure and connect it to the sample applications in `samples/19-postgres`.

The PostgreSQL sample is split into three focused sub-projects:

| Sub-project | Path | Demonstrates |
|-------------|------|---------------|
| `crud` | `samples/19-postgres/crud` | Single-record CRUD operations with `JdbcClient` |
| `batch` | `samples/19-postgres/batch` | Bulk inserts using `NamedParameterJdbcTemplate.batchUpdate()` |
| `transactions` | `samples/19-postgres/transactions` | Atomic money transfers with `@Transactional` |

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

## 2. Run a Spring Boot Sample

Choose the sub-project you want to run and set the required database environment variables:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=tutorial
export DB_USER=tutorial
export DB_PASSWORD=tutorial
```

Then start the chosen sub-project:

```bash
# CRUD
cd samples/19-postgres/crud
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Batch
cd samples/19-postgres/batch
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Transactions
cd samples/19-postgres/transactions
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Each sub-project exposes Swagger UI at `http://localhost:8080/swagger-ui.html` when running with the development profile.

## 3. Test Endpoints

### CRUD

Create a user:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}' \
  http://localhost:8080/api/v1/users
```

List users:

```bash
curl http://localhost:8080/api/v1/users
```

### Batch

Batch create users:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"users":[{"name":"Alice","email":"alice@example.com"},{"name":"Bob","email":"bob@example.com"}]}' \
  http://localhost:8080/api/v1/users/batch
```

### Transactions

Fetch an account:

```bash
curl http://localhost:8080/api/v1/accounts/1
```

Transfer funds between accounts:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":100.00}' \
  http://localhost:8080/api/v1/accounts/transfer
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
