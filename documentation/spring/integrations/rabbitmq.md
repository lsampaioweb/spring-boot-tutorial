## RabbitMQ

This tutorial compares four RabbitMQ exchange types using four isolated Spring Boot sample applications.

Objectives:

1. Understand routing behavior for Direct, Fanout, Topic, and Headers exchanges.
1. Keep each example simple and runnable on its own.
1. Compare patterns side-by-side without mixing concerns in one project.

### Why RabbitMQ instead of direct REST calls?

REST is synchronous. The caller waits for a response and is coupled to receiver availability.

RabbitMQ is asynchronous. The producer publishes and continues while consumers process when ready.

Use RabbitMQ when you need:

1. Decoupled producer and consumer lifecycles.
1. Reliable delivery and broker-backed buffering.
1. Flexible routing to one or many consumers.

### Project structure

Path: `samples/18-rabbitmq`

Each subproject is an independent runnable app:

1. `direct` - Direct exchange example.
1. `fanout` - Fanout exchange example.
1. `topic` - Topic exchange example.
1. `headers` - Headers exchange example.

Each project contains the same core pieces:

1. `RabbitMQConfiguration` - queue/exchange/binding declarations.
1. `MessageProducer` - publishes `OrderMessage`.
1. `MessageConsumer` - consumes routed messages.
1. `OrderApi` - HTTP entry point to publish test messages.

### Exchange comparison

| Type | Subproject | Port | Endpoint | Routing strategy |
|---|---|---:|---|---|
| Direct  | `direct`  | `8080` | `/api/v1/messages/direct`  | Exact routing key match |
| Fanout  | `fanout`  | `8082` | `/api/v1/messages/fanout`  | Broadcast to all bound queues |
| Topic   | `topic`   | `8083` | `/api/v1/messages/topic`   | Pattern routing with wildcards |
| Headers | `headers` | `8084` | `/api/v1/messages/headers` | Match by message header values |

### Start RabbitMQ Infrastructure

```bash
cd samples/infrastructure/rabbitmq
podman compose up -d
```

RabbitMQ management UI:
1. URL: `http://localhost:15672`
1. User: `admin`
1. Password: `admin`

### Run each sample

Open one terminal per subproject:

```bash
cd samples/18-rabbitmq/direct
mvn spring-boot:run
```

```bash
cd samples/18-rabbitmq/fanout
mvn spring-boot:run
```

```bash
cd samples/18-rabbitmq/topic
mvn spring-boot:run
```

```bash
cd samples/18-rabbitmq/headers
mvn spring-boot:run
```

### Test each exchange

Common payload fields are sent as query parameters (`customerName`, `product`, `quantity`, `price`).

Direct:

```bash
curl -X POST "http://localhost:8080/api/v1/messages/direct?customerName=Alice&product=Laptop&quantity=1&price=999.99"
```

Fanout:

```bash
curl -X POST "http://localhost:8082/api/v1/messages/fanout?customerName=Bob&product=Mouse&quantity=2&price=49.90"
```

Topic (optional `routingKey`; defaults to configured key):

```bash
curl -X POST "http://localhost:8083/api/v1/messages/topic?customerName=Carol&product=Desk&quantity=1&price=299.00&routingKey=dev.order.created"
```

Headers (optional `headerValue`; defaults to configured value):

```bash
curl -X POST "http://localhost:8084/api/v1/messages/headers?customerName=Dave&product=Keyboard&quantity=1&price=129.00&headerValue=order.audit"
```

### What to observe in logs

1. Direct: message reaches the queue bound with the same routing key.
1. Fanout: one publish reaches all queues bound to the exchange.
1. Topic: message delivery depends on wildcard pattern matches.
1. Headers: delivery depends on message header values.

### Notes

1. Development profile is active by default in each subproject.
1. Infrastructure credentials are `admin/admin` from `samples/infrastructure/rabbitmq/docker-compose.yml`.
1. Management endpoints expose `health` and `metrics` in development.

### Stop RabbitMQ Infrastructure

```bash
cd samples/infrastructure/rabbitmq
podman compose down
```

[Go Back](../../../README.md)

