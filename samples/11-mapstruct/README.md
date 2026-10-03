# 11 — MapStruct

Compile-time DTO mapping for the Users CRUD API.

## Before you start

- Previous: [10-exception-handling](../10-exception-handling/README.md)
- Topic: [documentation/spring/intermediate/mapstruct.md](../../documentation/spring/intermediate/mapstruct.md)

## Run

```bash
cd samples/11-mapstruct
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i http://localhost:8080/api/v1/users
```

Expected: `HTTP/1.1 200` with user DTOs produced via MapStruct mappers.

## Tests

```bash
mvn test
```

Generated mapper implementations appear under `target/generated-sources/annotations`.

## Next

[12-http-client](../12-http-client/README.md)
