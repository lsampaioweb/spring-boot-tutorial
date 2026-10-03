# 07 — Actuator

Exposes Actuator endpoints (`health`, `info`, `metrics`) without a domain REST API.

## Before you start

- Previous: [06-i18n](../06-i18n/README.md)
- Topic: [documentation/spring/basic/actuator.md](../../documentation/spring/basic/actuator.md)

## Run

```bash
cd samples/07-actuator
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i http://localhost:8080/actuator/health
```

Expected: `HTTP/1.1 200` and a JSON body with `"status":"UP"` (or similar).

## Tests

```bash
mvn test
```

## Next

[08-restapi](../08-restapi/README.md)
