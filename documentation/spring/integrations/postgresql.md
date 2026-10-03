# Spring Boot + PostgreSQL

Working samples: `samples/21-postgres`. Infrastructure: `samples/infrastructure/postgres`. Redis `cache-layer` also uses this database.

The compose runbook (start, apply SQL, verify, stop) lives next to the files: [`samples/infrastructure/postgres/README.md`](../../../samples/infrastructure/postgres/README.md). This page is the Spring Boot side.

The application is expected to use **DML only**. DDL lives under each sample's `sql/db/` folder. Samples do **not** use `spring.sql.init`, Flyway, or Liquibase.

| Sub-project | Path | Demonstrates |
|-------------|------|--------------|
| `crud` | `samples/21-postgres/crud` | Single-record CRUD with `JdbcClient` |
| `batch` | `samples/21-postgres/batch` | Bulk inserts with `NamedParameterJdbcTemplate.batchUpdate()` |
| `transactions` | `samples/21-postgres/transactions` | Atomic transfers with `@Transactional` |

Commands below start from the **tutorial repo root**. Replace `docker` with `podman` if that is what you use.

## Prerequisites
1. Docker Compose, or Podman with Podman Compose
1. Java 25
1. Maven 3.9+

## 1. Start PostgreSQL

Follow [`samples/infrastructure/postgres/README.md`](../../../samples/infrastructure/postgres/README.md). Short version:

```bash
cd samples/infrastructure/postgres
cp .env.example .env
set -a && source .env && set +a
docker compose up -d
docker exec tutorial-postgres pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" -h 127.0.0.1
```

Apply SQL once (crud and batch share `users`). `.env` must still be loaded in this shell:

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/crud/src/main/resources/sql/db/schema.sql

docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/schema.sql
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/insert.sql
```

## 2. Configure the sample

Credentials are not in YAML. After `source` on the infrastructure `.env`:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME="$POSTGRES_DB"
export DB_USER="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"
```

```yaml
spring:
  datasource:
    url: "jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME}"
    username: "${DB_USER}"
    password: "${DB_PASSWORD}"
```

## 3. Run a sample

```bash
cd samples/21-postgres/crud
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Same command from `batch` or `transactions`. Swagger UI (development): `http://localhost:8080/swagger-ui/index.html`

## 4. Test endpoints

### CRUD

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}' \
  http://localhost:8080/api/v1/users

curl http://localhost:8080/api/v1/users
```

### Batch

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"users":[{"name":"Alice","email":"alice@example.com"},{"name":"Bob","email":"bob@example.com"}]}' \
  http://localhost:8080/api/v1/users/batch
```

### Transactions

```bash
curl http://localhost:8080/api/v1/accounts/1

curl -X POST -H "Content-Type: application/json" \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":100.00}' \
  http://localhost:8080/api/v1/accounts/transfer
```

## 5. Stop PostgreSQL

```bash
cd samples/infrastructure/postgres
docker compose down
```

Data remains in `samples/infrastructure/postgres/volumes`. After a wipe, re-apply the SQL files.

### Troubleshooting

Infrastructure failures (password vs existing volume, port 5432, PG 18 data path,
bind-mount permissions) are in
[`samples/infrastructure/postgres/README.md`](../../../samples/infrastructure/postgres/README.md).

Docker/Podman credential and rootless issues:
[containers.md](../../setup/containers.md).

## Next

[RabbitMQ](rabbitmq.md) — `samples/22-rabbitmq`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
