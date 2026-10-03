# 16 — Virtual threads

Enables virtual threads and exposes a blocking call to httpbin.org.

## Before you start

- Previous: [15-events](../15-events/README.md)
- Topic: [documentation/spring/advanced/virtual-threads.md](../../documentation/spring/advanced/virtual-threads.md)
- Outbound network access to `https://httpbin.org`

## Run

```bash
cd samples/16-virtual-threads
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i http://localhost:8080/api/v1/httpbins/block/1
```

Expected: `HTTP/1.1 200` after ~1 second. Logs may show a virtual thread name.

Load scripts (optional): `src/test/k6/` — see [20-k6](../20-k6/README.md).

## Tests

```bash
mvn test
```

## Next

[17-container](../17-container/README.md)
