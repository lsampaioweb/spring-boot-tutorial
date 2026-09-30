# Spring Boot + RabbitMQ

Working samples: `samples/22-rabbitmq`. Infrastructure: `samples/infrastructure/rabbitmq`.

The compose runbook (start, verify, stop) lives next to the files: [`samples/infrastructure/rabbitmq/README.md`](../../../samples/infrastructure/rabbitmq/README.md). This page is the Spring Boot side.

This tutorial compares four RabbitMQ exchange types using four isolated Spring Boot apps.

| Type | Subproject | Port | Endpoint | Routing strategy |
|------|------------|------|----------|------------------|
| Direct  | `direct`  | `8080` | `/api/v1/messages/direct`  | Exact routing key match |
| Fanout  | `fanout`  | `8082` | `/api/v1/messages/fanout`  | Broadcast to all bound queues |
| Topic   | `topic`   | `8083` | `/api/v1/messages/topic`   | Pattern routing with wildcards |
| Headers | `headers` | `8084` | `/api/v1/messages/headers` | Match by message header values |

Each project contains `RabbitMQConfiguration`, `MessageProducer`, `MessageConsumer`, and `OrderRestController`.

Commands below start from the **tutorial repo root**. Replace `docker` with `podman` if that is what you use.

## Prerequisites
1. Docker Compose, or Podman with Podman Compose
1. Java 25
1. Maven 3.9+

## 1. Start RabbitMQ

Follow [`samples/infrastructure/rabbitmq/README.md`](../../../samples/infrastructure/rabbitmq/README.md). Short version:

```bash
cd samples/infrastructure/rabbitmq
cp .env.example .env
# edit .env — do not commit it
set -a && source .env && set +a
docker compose up -d
docker exec tutorial-rabbitmq rabbitmq-diagnostics -q ping
```

Management UI: `http://localhost:15672` — use `RABBITMQ_DEFAULT_USER` and `RABBITMQ_DEFAULT_PASS` from `.env`. There is no password in YAML or compose.

Optional Traefik hostname `rabbitmq.lan.home`: uncomment the `labels:` block. AMQP stays on host port `5672`.

## 2. Run a sample

Keep the infrastructure `.env` loaded in the shell (`RABBITMQ_DEFAULT_USER` and `RABBITMQ_DEFAULT_PASS`). Host and port default to `localhost:5672`.

```yaml
spring:
  rabbitmq:
    host: "${RABBITMQ_HOST:localhost}"
    port: "${RABBITMQ_PORT:5672}"
    username: "${RABBITMQ_DEFAULT_USER}"
    password: "${RABBITMQ_DEFAULT_PASS}"
```

```bash
cd samples/22-rabbitmq/direct
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

Same command from `fanout`, `topic`, or `headers` (each uses its own development port).

## 3. Test each exchange

Common payload fields: `customerName`, `product`, `quantity`, `price`.

Direct:

```bash
curl -X POST http://localhost:8080/api/v1/messages/direct \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Alice","product":"Laptop","quantity":1,"price":999.99}'
```

Fanout:

```bash
curl -X POST http://localhost:8082/api/v1/messages/fanout \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Bob","product":"Mouse","quantity":2,"price":49.90}'
```

Topic (optional `routingKey`; defaults to the configured key):

```bash
curl -X POST http://localhost:8083/api/v1/messages/topic \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Carol","product":"Desk","quantity":1,"price":299.00,"routingKey":"dev.order.created"}'
```

Headers (optional `headerValue`; defaults to the configured value):

```bash
curl -X POST http://localhost:8084/api/v1/messages/headers \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Dave","product":"Keyboard","quantity":1,"price":129.00,"headerValue":"order.audit"}'
```

What to look for in logs:

1. Direct: the queue bound with the same routing key receives the message.
1. Fanout: one publish reaches every bound queue.
1. Topic: delivery depends on wildcard pattern matches.
1. Headers: delivery depends on message header values.

## 4. Stop RabbitMQ

```bash
cd samples/infrastructure/rabbitmq
docker compose down
```

Data remains in `samples/infrastructure/rabbitmq/volumes`. Default user/password are created only on first boot; after a wipe, `compose up` reads `.env` again.

### Troubleshooting

Infrastructure failures (`.env` missing, login vs existing volume, ports 5672/15672, bind-mount permissions) are in [`samples/infrastructure/rabbitmq/README.md`](../../../samples/infrastructure/rabbitmq/README.md).

If startup fails with `docker-credential-secretservice` missing while using Docker Compose, install Docker credential helpers or remove `credsStore` from `~/.docker/config.json`.

If `podman compose up` fails with `potentially insufficient UIDs or GIDs available in user namespace`, your rootless Podman user is missing subuid/subgid mappings. Ask an administrator to add ranges for your user in `/etc/subuid` and `/etc/subgid`, then run:

```bash
podman system migrate
```

Preflight check:

```bash
grep "^$(whoami):" /etc/subuid
grep "^$(whoami):" /etc/subgid
```

If either command returns no line, ask an administrator to add unique ranges, for example:

```bash
usermod --add-subuids 100000-165535 --add-subgids 100000-165535 <username>
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
