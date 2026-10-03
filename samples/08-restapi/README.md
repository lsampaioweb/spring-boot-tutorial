# 08 — REST API

In-memory Users CRUD with pagination/sorting and OpenAPI (Swagger UI in development).

## Before you start

- Previous: [07-actuator](../07-actuator/README.md)
- Topic: [documentation/spring/basic/rest.md](../../documentation/spring/basic/rest.md)

## Run

```bash
cd samples/08-restapi
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`. Swagger: `http://localhost:8080/swagger-ui/index.html`

## Try it

```bash
curl -i http://localhost:8080/api/v1/users
```

Expected: `HTTP/1.1 200` and a page of users (seed data includes at least user id `1`).

## Tests

```bash
mvn test
```

## Next

[09-validation](../09-validation/README.md)
