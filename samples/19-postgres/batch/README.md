<!-- filepath: README.md -->

# Postgres Batch Sample

Sample Spring Boot application demonstrating batch operations with PostgreSQL and Spring JDBC.

## Overview

This project showcases:
- **Batch Operations**: Bulk insert/update using `NamedParameterJdbcTemplate`
- **Spring JDBC**: Using Spring JDBC Template for database access without ORM
- **PostgreSQL**: PostgreSQL database for persistent storage
- **Input Validation**: Form validation with Bean Validation (`@NotBlank`, `@Email`, `@NotEmpty`)
- **Exception Handling**: Custom domain exceptions and global exception handler
- **i18n Support**: Internationalization with English and Portuguese (pt-BR)
- **OpenAPI**: Swagger UI documentation for API endpoints

## Prerequisites

- Java 25+
- Maven 3.9+
- PostgreSQL 12+
- Docker (optional, for containerized PostgreSQL)

## Running Locally

### 1. Start PostgreSQL

Using Docker:
```bash
docker run --name postgres-batch \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=mydatabase \
  -p 5432:5432 \
  postgres:15
```

### 2. Configure Environment

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

### 3. Build and Run

Run with development profile (port 8080, debug logging):
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=development"
```

Run with production profile (port 9443, minimal logging):
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=production"
```

### 4. Access the Application

- **REST API**: `http://localhost:8080/api/v1/users/batch`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
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
- `BatchUserController`: REST endpoints for batch operations

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
