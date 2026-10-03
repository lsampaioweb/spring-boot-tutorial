# RabbitMQ

Publish orders to four exchange types from isolated Spring Boot modules.

Working samples: [`samples/22-rabbitmq`](../../../samples/22-rabbitmq)
([catalog README](../../../samples/22-rabbitmq/README.md)).

| Module | Runbook | Endpoint (development port **8080**) |
| --- | --- | --- |
| Direct | [`direct/README.md`](../../../samples/22-rabbitmq/direct/README.md) | `POST /api/v1/messages/direct` |
| Fanout | [`fanout/README.md`](../../../samples/22-rabbitmq/fanout/README.md) | `POST /api/v1/messages/fanout` |
| Topic | [`topic/README.md`](../../../samples/22-rabbitmq/topic/README.md) | `POST /api/v1/messages/topic` |
| Headers | [`headers/README.md`](../../../samples/22-rabbitmq/headers/README.md) | `POST /api/v1/messages/headers` |

Infrastructure: [`samples/infrastructure/rabbitmq/README.md`](../../../samples/infrastructure/rabbitmq/README.md).

Run **one module at a time** — each development profile binds **8080**.

## Before you start

- Previous: [PostgreSQL](postgresql.md) — `samples/21-postgres`
- Docker/Podman, Java 25, Maven 3.9+
- Time: ~25 minutes for infra + one exchange

## Why this exists

In-process events stop at the JVM boundary. RabbitMQ exchanges show how the same
order payload can be routed by key (direct), broadcast (fanout), patterns
(topic), or headers. Each submodule has its own producer, consumer, and REST
entry point.

## What you will see

- Broker on AMQP **5672**, management UI **15672**
- `POST` → **202** with `orderId` and `"Message submitted successfully"`
- Producer / consumer log lines when the message reaches a bound queue

## Run

### 1) Start RabbitMQ

```bash
cd samples/infrastructure/rabbitmq
cp .env.example .env
# edit .env — do not commit it
set -a && source .env && set +a
docker compose up -d
docker exec tutorial-rabbitmq rabbitmq-diagnostics -q ping
```

Management UI: `http://localhost:15672` — use `RABBITMQ_DEFAULT_USER` /
`RABBITMQ_DEFAULT_PASS` from `.env`.

### 2) Run the direct sample (recommended first Try it)

Keep the same shell exports (required; no YAML password defaults):

```bash
export RABBITMQ_DEFAULT_USER=...   # from .env
export RABBITMQ_DEFAULT_PASS=...
export RABBITMQ_HOST=localhost     # optional
export RABBITMQ_PORT=5672          # optional

cd samples/22-rabbitmq/direct
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Swap `direct` for `fanout`, `topic`, or `headers` the same way (still port 8080).

## Try it

With **direct** running:

```bash
curl -i -X POST http://localhost:8080/api/v1/messages/direct \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9}'
```

Expected: `HTTP/1.1 202`, JSON with `orderId` (UUID) and
`"message":"Message submitted successfully"`.

Expected logs: producer `Order sent to broker...`, then consumer
`Order received...` / `Order processed successfully...`.

Other modules (one at a time on 8080):

```bash
# fanout
curl -i -X POST http://localhost:8080/api/v1/messages/fanout \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9}'

# topic — development bindings are dev.order.* and dev.audit.#
curl -i -X POST http://localhost:8080/api/v1/messages/topic \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9,"routingKey":"dev.order.created"}'

# headers — headerValue maps to eventType; bindings expect order.created or order.audit
curl -i -X POST http://localhost:8080/api/v1/messages/headers \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Ada","product":"Book","quantity":1,"price":9.9,"headerValue":"order.created"}'
```

## How the sample is shaped

| Concern | Location |
| --- | --- |
| REST entry | `OrderRestController` per module |
| Publish | `MessageProducer` |
| Consume | `MessageConsumer` (`@RabbitListener`) |
| Broker creds | `RABBITMQ_DEFAULT_USER` / `RABBITMQ_DEFAULT_PASS` |

## Tests

From a module folder (broker expected for context tests that activate development):

```bash
cd samples/22-rabbitmq/direct && mvn test
```

## Stop

`Ctrl+C` for the app, then:

```bash
cd samples/infrastructure/rabbitmq
docker compose down
```

## Next

[Redis](redis.md) — `samples/23-redis`.

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
