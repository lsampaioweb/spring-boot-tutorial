# Postgres CRUD Sample

Sample Spring Boot application demonstrating CRUD operations with PostgreSQL and Spring JDBC.

## Overview

This project showcases:
- **REST API**: HTTP-based user management (create, read, update, delete)
- **Spring JDBC**: Using Spring JDBC Template for database access without ORM
- **PostgreSQL**: PostgreSQL database for persistent storage
- **Input Validation**: Form validation with Bean Validation (`@NotBlank`, `@Email`)
- **Exception Handling**: Custom domain exceptions and global exception handler
- **i18n Support**: Internationalization with English and Portuguese (pt-BR)
- **OpenAPI**: Swagger UI documentation for API endpoints
- **Health Indicators**: Spring Boot Actuator health endpoint

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

Apply the `users` schema (shared with `batch`), from the **tutorial repo root**:

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

```bash
cd samples/21-postgres/crud
# Development profile (port 8080, debug logging)
mvn spring-boot:run -Dspring-boot.run.profiles=development

# Production profile (port 9443, minimal logging)
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

### 4. Access the Application

- **REST API**: `http://localhost:8080/api/v1/users`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`

## i18n Support

Responses are internationalized based on the `Accept-Language` header:

```bash
# English response
curl -H "Accept-Language: en" http://localhost:8080/api/v1/users

# Portuguese response
curl -H "Accept-Language: pt-BR" http://localhost:8080/api/v1/users
```

## API Endpoints

### Get All Users
```http
GET /api/v1/users
```

### Get User by ID
```http
GET /api/v1/users/{id}
```

### Create User
```http
POST /api/v1/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Update User
```http
PUT /api/v1/users/{id}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

### Delete User
```http
DELETE /api/v1/users/{id}
```

## Key Features

### Exception Handling
- Custom domain exceptions (`UserNotFoundException`, `DatabaseException`)
- Global `@RestControllerAdvice` handler
- Validation error handling with field-level details

### Health Monitoring
- Spring Boot Actuator health endpoints

### OpenAPI Documentation
- Auto-generated Swagger UI at `/swagger-ui/index.html`
- Detailed endpoint descriptions with `@Operation` and `@ApiResponse` annotations

## Configuration

### Profiles

**Development** (`application-development.yml`):
- Port: 8080
- Debug logging
- Swagger UI enabled
- Health details always shown
- Stack traces always included

**Production** (`application-production.yml`):
- Port: 9443
- Info logging only
- Swagger UI disabled
- Health details never shown
- Stack traces never included

## Troubleshooting

### Database Connection Error
- Verify PostgreSQL is running
- Check environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- Review logs: `tail -f logs/application.log`

### Validation Errors
- All fields are required: `name`, `email`
- Email must be valid format
- Name cannot be blank

### Port Already in Use
Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

## Tests

Run from this module directory:

```bash
mvn test
```

The context-load test requires the `DB_*` environment variables described above.

## License

See LICENSE file in project root.

## Created by

Luciano Sampaio
