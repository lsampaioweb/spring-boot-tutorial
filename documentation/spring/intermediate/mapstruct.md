# MapStruct

Generate DTO mappers at compile time instead of hand-written mapping beans.

Working sample: [`samples/11-mapstruct`](../../../samples/11-mapstruct). Runbook:
[`samples/11-mapstruct/README.md`](../../../samples/11-mapstruct/README.md).

## Before you start

- Previous: [Exception Handling](exception-handling.md) — `samples/10-exception-handling`
- Java 25, Maven 3.9+
- Time: ~15 minutes

## Why this exists

Manual mappers grow verbose and drift from fields. MapStruct generates
implementations during `mvn compile` (`@Mapper(componentModel = "spring")`). The
Users API still looks familiar, but list returns a plain JSON array (not a page).

## What you will see

- App on **8080**
- `GET /api/v1/users` → **200** with three seeded users
- Generated sources under `target/generated-sources/annotations` after compile

## Run

```bash
cd samples/11-mapstruct
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

```bash
curl -i http://localhost:8080/api/v1/users
```

Expected: `HTTP/1.1 200` and a JSON **array** of user DTOs (for example
`user-01` … `user-03`), produced via MapStruct `toResponse` / entity methods.

Swagger (development): `http://localhost:8080/swagger-ui/index.html`

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `UserMapper` | MapStruct `@Mapper(componentModel = "spring")` |
| `UserServiceImpl` | Calls `toNewEntity` / `toEntity` / `toResponse` |
| `UserRestController` | CRUD under `/api/v1/users` (list is `List`, not `Page`) |
| `pom.xml` | MapStruct dependency + annotation processor path |

## Tests

```bash
cd samples/11-mapstruct && mvn test
```

Context load + i18n consistency. Compile also proves the generated mapper exists.

## Stop

`Ctrl+C`.

## Previous

[Exception Handling](exception-handling.md).

## Next
[HTTP Client](http-client.md) — `samples/12-http-client`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
