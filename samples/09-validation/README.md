# 09 — Validation

Bean Validation on the Users API (`@Valid`, i18n error messages).

## Before you start

- Previous: [08-restapi](../08-restapi/README.md)
- Topic: [documentation/spring/intermediate/validation.md](../../documentation/spring/intermediate/validation.md)

## Run

```bash
cd samples/09-validation
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

Valid create:

```bash
curl -i -X POST http://localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada","email":"ada@example.com"}'
```

Expected: `HTTP/1.1 201` with a `Location` header.

Invalid create:

```bash
curl -i -X POST http://localhost:8080/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"","email":"not-an-email"}'
```

Expected: `HTTP/1.1 400` with validation error details.

## Tests

```bash
mvn test
```

## Next

[10-exception-handling](../10-exception-handling/README.md)
