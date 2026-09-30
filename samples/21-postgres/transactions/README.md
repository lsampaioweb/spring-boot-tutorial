<!-- filepath: README.md -->

# Postgres Transactions Sample

Sample Spring Boot application demonstrating transactional money transfer operations with PostgreSQL and Spring JDBC.

## Overview

This sample demonstrates:
1. Atomic transfer operations with @Transactional.
1. Spring JDBC data access without ORM.
1. Domain exceptions with i18n messages.
1. Global API exception handling.
1. OpenAPI endpoint documentation.

## Prerequisites

- Java 25+
- Maven 3.9+
- PostgreSQL (the included Compose service uses version 18)

## Running Locally

Use the shared stack. Copy `.env.example` to `.env` and change the values there — not in YAML:

```bash
cd samples/infrastructure/postgres
cp .env.example .env
set -a && source .env && set +a
docker compose up -d
cd ../../.. # Return to the tutorial repo root for the SQL commands below.
```

Apply schema and seed, from the **tutorial repo root**:

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/schema.sql
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/transactions/src/main/resources/sql/db/insert.sql
```

Full runbook: [`samples/infrastructure/postgres/README.md`](../../infrastructure/postgres/README.md).

### Configuration

Map the infrastructure `.env` onto the names this app reads (`DB_*` has no default in YAML):

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME="$POSTGRES_DB"
export DB_USER="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"
```

Run in development profile:
```bash
cd samples/21-postgres/transactions
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Run in production profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

## Access

- Account: `GET http://localhost:8080/api/v1/accounts/1`
- Transfer: `POST http://localhost:8080/api/v1/accounts/transfer`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`

## API Summary

- GET /api/v1/accounts/{id}: fetch an account by id
- POST /api/v1/accounts/transfer: transfer amount from one account to another

Sample transfer request:
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100.00
}
```

## Tests

Run from this module directory:

```bash
mvn test
```

## Created by

Luciano Sampaio
