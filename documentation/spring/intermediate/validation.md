# Validation

Reject invalid request bodies with Bean Validation (`@Valid`) and i18n messages.

Working sample: [`samples/09-validation`](../../../samples/09-validation). Runbook:
[`samples/09-validation/README.md`](../../../samples/09-validation/README.md).

## Before you start

- Previous: [REST](../basic/rest.md) — `samples/08-restapi`
- Java 25, Maven 3.9+
- Time: ~15 minutes

## Why this exists

Controllers should not trust client JSON. `spring-boot-starter-validation` plus
`@Valid` on the DTO stops bad payloads before the service layer. Messages come
from the same `messages.properties` style as i18n.

## What you will see

- Valid `POST` → **201** with a created user body and a `Location` header
- Invalid `POST` → **400** with field errors (for example “Name is required”, “Email is invalid”)

## Run

```bash
cd samples/09-validation
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`. Swagger (development): `http://localhost:8080/swagger-ui/index.html`

## Try it

Valid create:

```bash
curl -i -X POST http://localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada","email":"ada@example.com"}'
```

Expected: `HTTP/1.1 201`, body includes `"name":"Ada"`, and a `Location` header
(sample builds it with `uriBuilder.path("/{id}")`).

Invalid create:

```bash
curl -i -X POST http://localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"","email":"not-an-email"}'
```

Expected: `HTTP/1.1 400` with Spring Boot’s validation error JSON (`status`,
`error`, `message`, `errors`, …). This sample has **no**
`@RestControllerAdvice` yet — that arrives in the next topic.

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `UserRestController` | `@Valid` on POST/PUT |
| `UserRequest` | `@NotBlank`, `@Email` with i18n message keys |
| `UserServiceImpl` | Same in-memory Users CRUD pattern as 08 |
| `i18n/messages*.properties` | Constraint messages |

## Tests

```bash
cd samples/09-validation && mvn test
```

Context load + i18n consistency.

## Stop

`Ctrl+C`.

## Previous

[REST](../basic/rest.md).

## Next
[Exception Handling](exception-handling.md) — `samples/10-exception-handling`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
