# Exception Handling

Return a structured JSON error body from a centralized `@RestControllerAdvice`.

Working sample: [`samples/10-exception-handling`](../../../samples/10-exception-handling).
Runbook: [`samples/10-exception-handling/README.md`](../../../samples/10-exception-handling/README.md).

## Before you start

- Previous: [Validation](validation.md) — `samples/09-validation`
- Java 25, Maven 3.9+
- Time: ~15 minutes

## Why this exists

Without advice, missing entities often become empty 404s or raw stack traces.
This sample maps domain exceptions to an `ErrorResponse` (`errorCode`, `message`,
`path`, …) for **Users** and **Products** APIs in the same app.

## What you will see

- Both feature APIs: `/api/v1/users` and `/api/v1/products`
- Missing resource → **404** with a stable `errorCode`
- Swagger UI in development

## Run

```bash
cd samples/10-exception-handling
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

Missing user:

```bash
curl -i http://localhost:8080/api/v1/users/999
```

Expected: `HTTP/1.1 404` and a body like:

```json
{
  "errorCode": "USER_NOT_FOUND",
  "message": "User with id \"999\" was not found.",
  "path": "/api/v1/users/999",
  "fields": null
}
```

(Development may also include a `trace` field.)

Missing product (second API in this sample):

```bash
curl -i http://localhost:8080/api/v1/products/999
```

Expected: `HTTP/1.1 404` with `"errorCode":"PRODUCT_NOT_FOUND"`.

Known seed (ids `1`–`10` for each API):

```bash
curl -i http://localhost:8080/api/v1/users/1
```

Expected: `HTTP/1.1 200` user JSON.

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `UserRestController` / `ProductRestController` | Feature HTTP APIs |
| `GlobalExceptionHandler` | `@RestControllerAdvice` |
| `ErrorResponse` / `ValidationError` | Error payload shape |
| `UserNotFoundException` / product equivalent | Domain → i18n key → `errorCode` |

## Tests

```bash
cd samples/10-exception-handling && mvn test
```

Context load + i18n consistency.

## Stop

`Ctrl+C`.

## Next

[MapStruct](mapstruct.md) — `samples/11-mapstruct`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
