<!-- filepath: README.md -->

# Postgres Batch Sample

Sample Spring Boot application demonstrating batch operations with PostgreSQL and Spring JDBC.

## Overview

This project showcases:
- **Batch Operations**: Bulk insert/update using `NamedParameterJdbcTemplate`
- **Spring JDBC**: Using `JdbcClient` for reads and `NamedParameterJdbcTemplate` for batch writes
- **PostgreSQL**: PostgreSQL database for persistent storage
- **Input Validation**: Form validation with Bean Validation (`@NotBlank`, `@Email`, `@NotEmpty`)
- **Exception Handling**: Custom domain exceptions and global exception handler
- **i18n Support**: Internationalization with English and Portuguese (pt-BR)
- **OpenAPI**: Swagger UI documentation for API endpoints

## Prerequisites

- Java 25+
- Maven 3.9+
- PostgreSQL (the included Compose service uses version 18)
- Docker (optional, for containerized PostgreSQL)

## Running Locally

### 1. Start PostgreSQL

Use the shared stack. Copy `.env.example` to `.env` and change the values there — not in YAML:

```bash
cd samples/infrastructure/postgres
cp .env.example .env
set -a && source .env && set +a
docker compose up -d
cd ../../.. # Return to the tutorial repo root for the SQL command below.
```

Apply the `users` schema (same file as `crud`), from the **tutorial repo root**:

```bash
docker exec -i tutorial-postgres psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  < samples/21-postgres/crud/src/main/resources/sql/db/schema.sql
```

Full runbook: [`samples/infrastructure/postgres/README.md`](../../infrastructure/postgres/README.md).

### 2. Configuration

Required. Map the infrastructure `.env` onto the names this app reads (`DB_*` has no default in YAML):

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME="$POSTGRES_DB"
export DB_USER="$POSTGRES_USER"
export DB_PASSWORD="$POSTGRES_PASSWORD"
```

### 3. Build and Run

Run with development profile (port 8080, debug logging):
```bash
cd samples/21-postgres/batch
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Run with production profile (port 9443, minimal logging):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

### 4. Access the Application

- **REST API**: `http://localhost:8080/api/v1/users/batch`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`

## i18n Support

Responses are internationalized based on the `Accept-Language` header:

Request in English:
```bash
curl -H "Accept-Language: en" http://localhost:8080/api/v1/users/batch
```

Request in Portuguese:
```bash
curl -H "Accept-Language: pt-BR" http://localhost:8080/api/v1/users/batch
```

## API Endpoints

### Batch Create Users
```http
POST /api/v1/users/batch
Content-Type: application/json

{
  "users": [
    {
      "name": "John Doe",
      "email": "john@example.com"
    },
    {
      "name": "Jane Smith",
      "email": "jane@example.com"
    }
  ]
}
```

Response:
```json
{
  "totalRequested": 2,
  "totalProcessed": 2,
  "createdUsers": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com"
    }
  ],
  "failedUsers": []
}
```

## Architecture

### Components

**Domain Model**
- `User`: Immutable record representing a user

**DTOs**
- `BatchCreateUserRequest`: Request for batch operations
- `BatchOperationResponse`: Response with operation results
- `UserResponse`: Standard user response

**Service Layer**
- `BatchUserService`: Interface for batch operations
- `BatchUserServiceImpl`: Implementation with `@Transactional` support

**Repository Layer**
- `UserRepository`: Data access with batch operations using `NamedParameterJdbcTemplate`
- `UserSqlColumns`: Centralized SQL column name constants

**Controllers**
- `BatchUserRestController`: REST endpoints for batch operations

**Exception Handling**
- `GlobalExceptionHandler`: Centralized `@RestControllerAdvice`
- `DatabaseException`: Domain exception for database errors

**i18n**
- `LogMessages`: English log messages component
- `I18nLocaleResolverConfig`: Accept-Language header resolution
- `messages.properties`, `messages_pt_BR.properties`: Message bundles

## Database Schema

```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
```

## Key Differences from CRUD Sub-Project

1. **Batch Operations**: Uses `NamedParameterJdbcTemplate.batchUpdate()` instead of single-record operations
2. **Response Aggregation**: Returns results grouped by success/failure
3. **Named Parameters**: SQL uses `:name` syntax for named parameters (`:name`, `:email`)
4. **No Delete**: Batch sub-project focuses on bulk inserts

## Notes

- Batch insert uses `NamedParameterJdbcTemplate` because `JdbcClient` does not have a batch API
- All column names use `UserSqlColumns` constants to prevent hardcoding
- Service layer applies `@Transactional` for atomicity: all users succeed or all fail
- Failed users are tracked separately in the response for visibility
- Validation happens before database operations via `@Valid` annotation

## Tests

Run from this module directory:

```bash
mvn test
```

## Created by

Luciano Sampaio
