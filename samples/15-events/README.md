# 15 — Application events

Publishes an in-process event when a message is created; an audit listener reacts.

## Before you start

- Previous: [14-https](../14-https/README.md)
- Topic: [documentation/spring/intermediate/events.md](../../documentation/spring/intermediate/events.md)

## Run

```bash
cd samples/15-events
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i -X POST http://localhost:8080/api/v1/messages \
  -H 'Content-Type: application/json' \
  -d '{"sender":"ada","content":"hello"}'
```

Expected: `HTTP/1.1 202`; console shows an audit log line from the event listener.

## Tests

```bash
mvn test
```

## Next

[16-virtual-threads](../16-virtual-threads/README.md)
