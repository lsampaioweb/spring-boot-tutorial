# 10 — Exception handling

Centralized `@RestControllerAdvice` for Users and Products APIs.

## Before you start

- Previous: [09-validation](../09-validation/README.md)
- Topic: [documentation/spring/intermediate/exception-handling.md](../../documentation/spring/intermediate/exception-handling.md)

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

Expected: `HTTP/1.1 404` with a structured error body.

Products API (second feature in this sample):

```bash
curl -i http://localhost:8080/api/v1/products
```

## Tests

```bash
mvn test
```

## Next

[11-mapstruct](../11-mapstruct/README.md)
