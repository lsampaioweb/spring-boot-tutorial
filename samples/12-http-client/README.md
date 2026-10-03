# 12 — HTTP client

Users API that proxies to another service with `RestClient`.

## Before you start

- Previous: [11-mapstruct](../11-mapstruct/README.md)
- Topic: [documentation/spring/intermediate/http-client.md](../../documentation/spring/intermediate/http-client.md)
- Start [08-restapi](../08-restapi/README.md) first (port `8080`) — this sample calls it

## Run

Terminal 1 — upstream:

```bash
cd samples/08-restapi
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Terminal 2 — client (port `8081`):

```bash
cd samples/12-http-client
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

```bash
curl -i http://localhost:8081/api/v1/users
```

Expected: `HTTP/1.1 200` with users fetched from the upstream on `8080`.

## Tests

```bash
mvn test
```

## Next

[13-thymeleaf](../13-thymeleaf/README.md)
