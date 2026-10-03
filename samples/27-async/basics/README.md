# 27 — Async basics

Accept-and-poll job API backed by `@Async` workers and HTTP Basic auth.

## Before you start

- Topic: [documentation/spring/intermediate/async.md](../../../documentation/spring/intermediate/async.md)

## Run

```bash
cd samples/27-async/basics
export ASYNC_USER_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`. Default username: `async-user`.

## Try it

```bash
curl -i -u async-user:change-me -X POST http://localhost:8080/api/v1/jobs \
  -H 'Content-Type: application/json' \
  -d '{"input":"demo","failForDemo":false,"delayMs":200}'
```

Expected: `HTTP/1.1 202` with a `Location` header pointing at the job status.

```bash
# use the id from Location
curl -i -u async-user:change-me http://localhost:8080/api/v1/jobs/<id>
```

Expected: status moves to succeeded (or failed if `failForDemo` is true).

## Tests

```bash
mvn test
```

## Next

[28-tracing](../../28-tracing/README.md)
