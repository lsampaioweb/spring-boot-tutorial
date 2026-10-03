# 23 — Redis pub/sub events

Publishes product events on a Redis channel and lists received events.

## Infrastructure

```bash
cd samples/infrastructure/redis
docker compose up -d
```

## Run

```bash
cd samples/23-redis/pubsub-events
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Port: `8080`.

## Try it

```bash
curl -i -X POST http://localhost:8080/api/v1/product-events \
  -H 'Content-Type: application/json' \
  -d '{"productId":1,"productName":"Book"}'
```

```bash
curl -i http://localhost:8080/api/v1/product-events/received
```

Expected: publish succeeds; received list includes the event.
