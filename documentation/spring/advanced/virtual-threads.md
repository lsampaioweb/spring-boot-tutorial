# Virtual Threads

Enable Spring Boot virtual threads and call a blocking external delay endpoint.

Working sample: [`samples/16-virtual-threads`](../../../samples/16-virtual-threads).
Runbook: [`samples/16-virtual-threads/README.md`](../../../samples/16-virtual-threads/README.md).

## Before you start

- Previous: [Async](../intermediate/async.md) — `samples/27-async/basics`
- Java 25, Maven 3.9+
- Outbound HTTPS to `https://httpbin.org`
- Time: ~15 minutes

## Why this exists

Blocking I/O on platform threads limits concurrency. With
`spring.threads.virtual.enabled: true`, Spring can run request handling on
virtual threads so a slow outbound call does not monopolize a scarce carrier
thread pool. This sample blocks on httpbin’s delay API and returns the thread
name in JSON.

## What you will see

- App on **8080**
- `GET /api/v1/httpbins/block/1` waits ~1s then returns **200**
- Response includes `statusCode` and a `thread` string (often a virtual thread)
- Optional k6 script under `src/test/k6/`

## Run

```bash
cd samples/16-virtual-threads
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

## Try it

```bash
curl -i http://localhost:8080/api/v1/httpbins/block/1
```

Expected: `HTTP/1.1 200` after about one second, body like
`{"statusCode":200,"thread":"..."}` (exact thread string varies).

Logs may include `HttpBin delay request completed. status=200, thread=...`.

Optional load script (install k6 first — see [K6](../tests/k6.md)):

```bash
k6 run samples/16-virtual-threads/src/test/k6/01-block-3.js
```

That script calls `/api/v1/httpbins/block/3` (default `BASE_URL=http://localhost:8080`).

## How the sample is shaped

| File / class | Role |
| --- | --- |
| `application.yml` | `spring.threads.virtual.enabled: true` |
| `HttpBinRestController` | `/api/v1/httpbins/block/{seconds}` |
| `HttpBinServiceImpl` | Blocking call to `https://httpbin.org/delay/...` |
| `src/test/k6/01-block-3.js` | Optional load check |

## Tests

```bash
cd samples/16-virtual-threads && mvn test
```

Context load + i18n consistency (k6 is separate).

## Stop

`Ctrl+C`.

## Next

[Container](../extra/container.md) — `samples/17-container`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
