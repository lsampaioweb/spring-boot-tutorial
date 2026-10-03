# REST

Build an in-memory Users CRUD API with OpenAPI (Swagger UI in development).

Working sample: [`samples/08-restapi`](../../../samples/08-restapi). Runbook:
[`samples/08-restapi/README.md`](../../../samples/08-restapi/README.md).

## Before you start

- Previous: [Actuator](actuator.md) — `samples/07-actuator`
- Java 25, Maven 3.9+
- Time: ~20 minutes

## Why this exists

REST is the main HTTP boundary for later samples (validation, exceptions,
MapStruct, HTTP client). This module seeds users in memory and maps
`/api/v1/users` with DTOs and a manual mapper — no database yet.

## What you will see

- App on **8080** (`development`)
- Seeded users with ids `1`–`10`
- Swagger UI at `http://localhost:8080/swagger-ui/index.html`
- `GET /api/v1/users/{id}` returns JSON or 404

## Run

```bash
cd samples/08-restapi
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

```bash
curl -i http://localhost:8080/api/v1/users/1
```

Expected: `HTTP/1.1 200` and a body like:

```json
{"id":1,"name":"user-01","email":"user-01@example.com"}
```

Missing id:

```bash
curl -i http://localhost:8080/api/v1/users/999
```

Expected: `HTTP/1.1 404` (empty body in this sample — structured errors come in
[Exception Handling](../intermediate/exception-handling.md)).

Controller surface (also in Swagger):

| Method | Path | Typical status |
| --- | --- | --- |
| `GET` | `/api/v1/users` | paginated list |
| `GET` | `/api/v1/users/{id}` | 200 / 404 |
| `POST` | `/api/v1/users` | 201 |
| `PUT` | `/api/v1/users/{id}` | 200 / 404 |
| `DELETE` | `/api/v1/users/{id}` | 204 / 404 |

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `RestapiApplication` | `@EnableSpringDataWebSupport` so `Pageable` binds from query params |
| `UserRestController` | HTTP mapping, `@PageableDefault` on list |
| `UserService` / `UserServiceImpl` | In-memory CRUD |
| `UserMapper` | Manual DTO mapping (`@Component`) |
| `OpenApiConfig` | springdoc / Swagger (development) |
| `i18n/messages*.properties` | Message keys for later error/validation reuse |

Without `@EnableSpringDataWebSupport` (or a Spring Data starter that registers it),
`GET /api/v1/users` fails with a 500 about constructing the `Pageable` interface.

## Tests

```bash
cd samples/08-restapi && mvn test
```

Context load plus `I18nConsistencyTest` for message key parity.

## Stop

`Ctrl+C`.

## Previous

[Actuator](actuator.md).

## Next
[Validation](../intermediate/validation.md) — `samples/09-validation`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
