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
- PostgreSQL 12+

## Running Locally

Start PostgreSQL container:
```bash
docker run --name postgres-transactions -e POSTGRES_PASSWORD=password -e POSTGRES_DB=mydatabase -p 5432:5432 postgres:15
```

Set database host:
```bash
export DB_HOST=localhost
```

Set database port:
```bash
export DB_PORT=5432
```

Set database name:
```bash
export DB_NAME=mydatabase
```

Set database user:
```bash
export DB_USER=postgres
```

Set database password:
```bash
export DB_PASSWORD=password
```

Run in development profile:
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=development"
```

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
