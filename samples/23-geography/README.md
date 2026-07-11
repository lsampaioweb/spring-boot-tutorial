<!-- filepath: samples/23-geography/README.md -->
# Geography API

## Overview

REST API for managing geographic data: countries, states, and cities.

## Prerequisites

- Java 25
- Maven 3.9+
- PostgreSQL 16+

## Running locally

```bash
# Database
export DB_USER=your_user
export DB_PASSWORD=your_password
# Optional — defaults: localhost, 5432, geography
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=geography

# Security — HTTP Basic credentials for write operations
export APP_SECURITY_USERNAME=admin
export APP_SECURITY_PASSWORD=your_password

mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Authentication

The API uses **HTTP Basic** authentication.

| Operations | Requirement |
|---|---|
| `GET` endpoints | Public — no credentials required |
| `POST`, `PUT`, `DELETE` endpoints | Requires the `EDITOR` role |

Set `APP_SECURITY_USERNAME` and `APP_SECURITY_PASSWORD` before starting the application. Pass them as `Authorization: Basic <base64>` on write requests.

## Endpoints

Paginated list endpoints accept optional query parameters:

| Parameter | Default | Max | Description |
|---|---|---|---|
| `page` | `0` | — | Zero-based page index |
| `size` | `20` | `100` | Items per page |

Response shape for list endpoints:

```json
{
  "items": [...],
  "page": 0,
  "size": 20,
  "totalElements": 42,
  "totalPages": 3
}
```

| Method | Path | Auth | Description |
|---|---|---|---|
| `GET` | `/api/v1/countries` | — | List countries (paginated) |
| `GET` | `/api/v1/countries/{id}` | — | Get country by ID |
| `POST` | `/api/v1/countries` | EDITOR | Create country |
| `PUT` | `/api/v1/countries/{id}` | EDITOR | Update country |
| `DELETE` | `/api/v1/countries/{id}` | EDITOR | Delete country |
| `GET` | `/api/v1/states` | — | List states (paginated) |
| `GET` | `/api/v1/states/{id}` | — | Get state by ID |
| `POST` | `/api/v1/states` | EDITOR | Create state |
| `PUT` | `/api/v1/states/{id}` | EDITOR | Update state |
| `DELETE` | `/api/v1/states/{id}` | EDITOR | Delete state |
| `GET` | `/api/v1/cities` | — | List cities (paginated) |
| `GET` | `/api/v1/cities/{id}` | — | Get city by ID |
| `POST` | `/api/v1/cities` | EDITOR | Create city |
| `PUT` | `/api/v1/cities/{id}` | EDITOR | Update city |
| `DELETE` | `/api/v1/cities/{id}` | EDITOR | Delete city |

## Development tools

Swagger UI is available in the `development` profile at `http://localhost:8080/swagger-ui.html`.

Actuator endpoints `health`, `info`, and `metrics` are exposed at `/actuator/*`.
Health details are visible to authenticated users only.

## Production

Production requires TLS. Set the following in addition to the database and security variables:

```bash
# PEM format (recommended)
export SSL_CERTIFICATE_PATH=/path/to/cert.crt
export SSL_PRIVATE_KEY_PATH=/path/to/cert.key

# PKCS12 / PFX format (alternative — see application-production.yml)
# export SSL_KEYSTORE_PATH=/path/to/keystore.p12
# export SSL_KEYSTORE_PASSWORD=keystore_password

# Optional — defaults to 9443
export SERVER_PORT=9443
```
